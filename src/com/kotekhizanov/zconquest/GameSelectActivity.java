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
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameSelectActivity extends Activity {

	GameMechanics gameMechanics;

	EditText GameIDEditText;
	TextView LoginErrorTextTextView;
	Button connectToGameButton;
	Button createGameButton;

	boolean buttonPressed = false;
	boolean ErrorAcured = false;
	String ErrorText = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_game_select);

		gameMechanics = GameMechanics.get(getApplication());

		GameIDEditText = (EditText) findViewById(R.id.GameID);
		LoginErrorTextTextView = (TextView) findViewById(R.id.LoginErrorText);
		connectToGameButton = (Button) findViewById(R.id.connectToGame);
		createGameButton = (Button) findViewById(R.id.createGame);

		connectToGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!buttonPressed) {
					ResetError();
					buttonPressed = true;
					hideKeyboart();

					MyAsyncTask asyncTask = new MyAsyncTask(true);
					asyncTask.execute();
				}
			}
		});

		createGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!buttonPressed) {
					ResetError();
					buttonPressed = true;
					hideKeyboart();

					MyAsyncTask asyncTask = new MyAsyncTask(false);
					asyncTask.execute();
				}
			}
		});

	}

	void hideKeyboart() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(GameIDEditText.getWindowToken(), 0);
	}

	void ResetError() {
		ErrorAcured = false;
		ErrorText = "";
		LoginErrorTextTextView.setText(ErrorText);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

		boolean ConnectToGame;

		public MyAsyncTask(boolean ConnectToGame) {
			super();

			this.ConnectToGame = ConnectToGame;
		}

		@Override
		protected Double doInBackground(String... params) {
			postData(gameMechanics.ServerUrl + "GameLogics.php", ConnectToGame);
			return null;
		}

		protected void onPostExecute(Double result) {
			// pb.setVisibility(View.GONE);

			buttonPressed = false;
			if (ErrorAcured) {
				Log.w("myApp", ErrorText);
				LoginErrorTextTextView.setText(ErrorText);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						GameActivity.class);
				startActivity(intent);
			}
			// openactivity

			// Toast.makeText(getApplicationContext(), "command sent",
			// Toast.LENGTH_LONG).show();
		}
	}

	public void postData(String url, boolean ConnectToGame) {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(url);
		try {

			ErrorAcured = false;
			ErrorText = "";

			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			
			

			nameValuePairs.add(new BasicNameValuePair("userId", Integer
					.toString(gameMechanics.CurrentPlayer.PlayerId)));
			if (ConnectToGame) {
				nameValuePairs.add(new BasicNameValuePair("functionName", "ConnectToGame"));
				nameValuePairs.add(new BasicNameValuePair("gameId",
						GameIDEditText.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("ConnectToGame",
						"true"));
			}
			else
			{
				nameValuePairs.add(new BasicNameValuePair("functionName", "CreateGame"));
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
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// process execption
		} catch (IOException e) {
			// process execption
		}
	}

	private void parseXML(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		int eventType = parser.getEventType();

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
				} else if (name.equals("gameId")) {
					gameMechanics.GameID = Integer.parseInt(parser.nextText());
				} else if (name.equals("pending")) {
					String PendingText = parser.nextText();
					if (PendingText.equals("1")) {
						gameMechanics.PendingGame = true;
					} else {
						gameMechanics.PendingGame = false;
					}
				} else if (name.equals("creator_userId")) {
					gameMechanics.GameCreatorUserID = Integer.parseInt(parser
							.nextText());
				}
				break;
			case XmlPullParser.END_TAG:

			}
			eventType = parser.next();
		}
	}
}
