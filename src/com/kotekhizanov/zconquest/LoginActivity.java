package com.kotekhizanov.zconquest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	GameMechanics gameMechanics;
	EditText LoginEditText;
	EditText PasswordEditText;
	TextView LoginErrorTextTextView;
	Button LoginButton;
	Button SignUpButton;
	boolean buttonPressed = false;

	SharedPreferences sPref;

	boolean ErrorAcured = false;
	String ErrorText = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameMechanics = GameMechanics.get(getApplication());

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		


		LoginEditText = (EditText) findViewById(R.id.login);
		PasswordEditText = (EditText) findViewById(R.id.password);
		LoginErrorTextTextView = (TextView) findViewById(R.id.LoginErrorText);
		LoginButton = (Button) findViewById(R.id.LoginButton);
		SignUpButton = (Button) findViewById(R.id.SignUpButton);

		RestoreLastLogin();

		LoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!buttonPressed)
				{
					ResetError();
					buttonPressed = true;
					hideKeyboart();
					
					MyAsyncTask asyncTask = new MyAsyncTask(false);
					asyncTask.execute();
				}
			}
		});

		SignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!buttonPressed)
				{
					ResetError();
					buttonPressed = true;
					hideKeyboart();
					
					MyAsyncTask asyncTask = new MyAsyncTask(true);
					asyncTask.execute();
				}
			}
		});

		/*
		 * 
		 * 
		 * setContentView(new GameView(this));
		 */
	}
	
	void hideKeyboart()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(LoginEditText.getWindowToken(), 0);
	}

	void SaveLastLogin() {
		sPref = getPreferences(MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString("LastLogin", LoginEditText.getText().toString());
		ed.putString("LastPassword", PasswordEditText.getText().toString());
		ed.commit();
		// Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
	}

	void RestoreLastLogin() {
		sPref = getPreferences(MODE_PRIVATE);
		String LastLogin = sPref.getString("LastLogin", "");
		String LastPassword = sPref.getString("LastPassword", "");
		LoginEditText.setText(LastLogin);
		PasswordEditText.setText(LastPassword);
		// Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
	}
	
	void ResetError()
	{
		ErrorAcured = false;
		ErrorText = "";
		LoginErrorTextTextView.setText(ErrorText);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
		
		boolean SignUp;
		
		public MyAsyncTask(boolean signUp) {
			super();

			SignUp = signUp;
		}

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(gameMechanics.ServerUrl + "login.php", SignUp);
			return null;
		}

		protected void onPostExecute(Double result) {
			// pb.setVisibility(View.GONE);
			
			buttonPressed = false;
			if(ErrorAcured)
			{
				Log.w("myApp", ErrorText);
				LoginErrorTextTextView.setText(ErrorText);
			}
			else
			{
				SaveLastLogin();
				
				Intent intent = new Intent(getApplicationContext(), GameSelectActivity.class);
			    startActivity(intent);
			}
			//openactivity
			
			// Toast.makeText(getApplicationContext(), "command sent",
			// Toast.LENGTH_LONG).show();
		}
	}

	public void postData(String url, boolean signUp) {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(url);
		try {

			ErrorAcured = false;
			ErrorText = "";
			
			List nameValuePairs = new ArrayList();

			nameValuePairs.add(new BasicNameValuePair("login", LoginEditText.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("password", PasswordEditText.getText().toString()));
			if (signUp)
			{
				nameValuePairs.add(new BasicNameValuePair("signup", "true"));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse hresponse = httpclient.execute(httppost);
			InputStream in = hresponse.getEntity().getContent();

			XmlPullParserFactory pullParserFactory;
			try {
				pullParserFactory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = pullParserFactory.newPullParser();

				// parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
				// false);
				parser.setInput(in, null);

				parseXML(parser);

			} catch (XmlPullParserException e) {

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// process execption
		} catch (IOException e) {
			// process execption
		}
	}

	private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = parser.getEventType();
		Player currentPlayer = null;

		while (eventType != XmlPullParser.END_DOCUMENT) {

			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (name.equals("error")) {
					ErrorAcured = true;
					ErrorText = parser.nextText();
				} else if (name.equals("player")) {
					currentPlayer = new Player();
				} else if (currentPlayer != null) {
					if (name.equals("PlayerId")) {
						currentPlayer.PlayerId = Integer.parseInt(parser.nextText());
					} else if (name.equals("PlayerName")) {
						currentPlayer.PlayerName = parser.nextText();
					}
				}
				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("player") && currentPlayer != null) {
					gameMechanics.CurrentPlayer = currentPlayer;
					
					Log.w("myApp", "id = " + gameMechanics.CurrentPlayer.PlayerId);
				}
			}
			eventType = parser.next();
		}
	}
}