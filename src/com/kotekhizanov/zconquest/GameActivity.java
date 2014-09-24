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
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements OnClickListener {

	GameMechanics gameMechanics;

	GameView gameView;
	DrawingSurface ds;
	FrameLayout frm;
	Button generatePlanetsButton;
	Button startGameButton;
	EditText planetAmountEditText;
	TextView GameIDTextView;
	RelativeLayout PendigGameMenuRL;
	RelativeLayout PlanetTurnMenuRL;
	
	LinearLayout SendShipsGroupRL;
	
	Button EndTurnButton;
	//TextView SelectedPLanetText;
	TextView SelectedPLanet;
	public TextView PlanetInfoShips;
	TextView PlanetInfoIncome;
	TextView PlanetInfoAttackRate;
	TextView SendShipsText;
	TextView SendShipsAmount;
	Button SendShipsSend;
	
	
	
	int color = 0xfff00000;

	boolean buttonPressed;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		gameMechanics = GameMechanics.get(getApplication());
		
		ds = new DrawingSurface(this, this);
		// gameView = new GameView(this);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_game_activity);

		frm = (FrameLayout) findViewById(R.id.frameLayout);
		frm.addView(ds);
		
		// frm.addView(gameView);

		PendigGameMenuRL = (RelativeLayout) findViewById(R.id.PendigGameMenu);		
		PlanetTurnMenuRL = (RelativeLayout) findViewById(R.id.PlanetTurnMenu);
		SendShipsGroupRL = (LinearLayout) findViewById(R.id.SendShipsGroup);
		planetAmountEditText = (EditText) findViewById(R.id.planetAmount);
		generatePlanetsButton = (Button) findViewById(R.id.generatePlanets);
		startGameButton = (Button) findViewById(R.id.startGame);
		GameIDTextView = (TextView) findViewById(R.id.GameID);
		
		EndTurnButton = (Button) findViewById(R.id.EndTurnButton);
		//SelectedPLanetText = (TextView) findViewById(R.id.SelectedPLanetText);
		SelectedPLanet = (TextView) findViewById(R.id.SelectedPLanet);
		PlanetInfoShips = (TextView) findViewById(R.id.PlanetInfoShips);
		PlanetInfoIncome = (TextView) findViewById(R.id.PlanetInfoIncome);
		PlanetInfoAttackRate = (TextView) findViewById(R.id.PlanetInfoAttackRate);
		SendShipsText = (TextView) findViewById(R.id.SendShipsText);
		SendShipsAmount = (TextView) findViewById(R.id.SendShipsAmount);
		SendShipsSend = (Button) findViewById(R.id.SendShipsSend);
		
		GameIDTextView.setText("Game ID: " + Integer.toString(gameMechanics.GameID));

		generatePlanetsButton.setOnClickListener(this);
		startGameButton.setOnClickListener(this);
		
		SendShipsGroupRL.setVisibility(View.INVISIBLE);
		
		setGameStatus();
		
	
		MyAsyncTask asyncTask = new MyAsyncTask("refresh");
		asyncTask.execute();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.generatePlanets:

			//buttonPressed = true;

			/*
			String planetAmountText = planetAmountEditText.getText().toString();
			gameMechanics.GeneratePlanets(Integer.parseInt(planetAmountText));
			*/

			MyAsyncTask asyncTaskGeneratePlanets = new MyAsyncTask("GeneratePlanets");
			asyncTaskGeneratePlanets.execute();
			

			break;

		case R.id.startGame:

			//buttonPressed = true;

			/*
			String planetAmountText = planetAmountEditText.getText().toString();
			gameMechanics.GeneratePlanets(Integer.parseInt(planetAmountText));
			*/

			MyAsyncTask asyncTaskStartGame = new MyAsyncTask("StartGame");
			asyncTaskStartGame.execute();
			

			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.w("myApp1", "OnDestroy");
		
		ds.mythread.setRunning(false);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ds.mythread.setRunning(false);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (ds.mythread != null)
		{			
			ds.mythread.setRunning(true);
		}
	}
	
	public void setGameStatus()
	{
		if(gameMechanics.PendingGame)
		{
			if(gameMechanics.CurrentPlayer.PlayerId == gameMechanics.GameCreatorUserID)
			{
				PendigGameMenuRL.setVisibility(View.VISIBLE);
			}
			else
			{
				PendigGameMenuRL.setVisibility(View.INVISIBLE);
			}
			PlanetTurnMenuRL.setVisibility(View.INVISIBLE);
		}
		else
		{
			PendigGameMenuRL.setVisibility(View.INVISIBLE);
			PlanetTurnMenuRL.setVisibility(View.VISIBLE);
		}
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
		
		String functionName;

		public MyAsyncTask(String functionName) {
			super();
			
			this.functionName = functionName;

			Log.w("myApphttp", functionName);

		}

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub

			
			postData(functionName);
			return null;
		}

		protected void onPostExecute(Double result) {

			GameIDTextView.setText("Game ID: " + Integer.toString(gameMechanics.GameID));
			buttonPressed = false;
			/*
			 * if (ErrorAcured) { Log.w("myApp", ErrorText);
			 * LoginErrorTextTextView.setText(ErrorText); } else { Intent intent
			 * = new Intent(getApplicationContext(), GameActivity.class);
			 * startActivity(intent); }
			 */
			
			if(functionName.equals("refresh"))
			{
				if(gameMechanics.PlanetPositionEdit)
				{
					return;
				}
				if(gameMechanics.PendingGame)
				{
					new CountDownTimer(1000, 1000) {
						
					     public void onFinish() {
							MyAsyncTask asyncTask = new MyAsyncTask("refresh");
							asyncTask.execute();
					     }
	
						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub
							
						}
					  }.start();
				}
				else
				{
					new CountDownTimer(3000, 1000) {
	
					     public void onFinish() {
							MyAsyncTask asyncTask = new MyAsyncTask("refresh");
							asyncTask.execute();
					     }
	
						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub
							
						}
					  }.start();
				}
			}
			
			setGameStatus();
		}
	}

	public void postData(String functionName) {
		HttpClient httpclient = new DefaultHttpClient();
		// specify the URL you want to post to
		HttpPost httppost = new HttpPost(gameMechanics.ServerUrl + "GameLogics.php");
		try {

			/*
			 * ErrorAcured = false; ErrorText = "";
			 */

			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			
			nameValuePairs.add(new BasicNameValuePair("functionName", functionName));

			nameValuePairs.add(new BasicNameValuePair("gameId", Integer
					.toString(gameMechanics.GameID)));
			nameValuePairs.add(new BasicNameValuePair("planetAmount",
					planetAmountEditText.getText().toString()));
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

	@SuppressWarnings("unused")
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		
		if(gameMechanics.PlanetPositionEdit)
		{
			return;
		}
		
		
		int eventType = parser.getEventType();
		Player newPlayer = null;
		Planet newPlanet = null;
		
		GameMechanics newGameMechanics = new GameMechanics(getApplication());

		while (eventType != XmlPullParser.END_DOCUMENT) {

			String name = null;

			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();

				if (name.equals("player")) {
					newPlayer = new Player();
					Log.w("myApp", "newPlayer");
				} else if (name.equals("userId") && !(newPlayer == null)) {
					newPlayer.PlayerId = Integer.parseInt(parser.nextText());

					Log.w("myApp", "newPlayer.PlayerId " + newPlayer.PlayerId);
				} else if (name.equals("userlogin") && !(newPlayer == null)) {
					newPlayer.PlayerName = parser.nextText();
					Log.w("myApp", "newPlayer.PlayerName " + newPlayer.PlayerName);
				}

				else if (name.equals("planet")) {
					newPlanet = new Planet();
					Log.w("myApp", "newPlanet");
				} else if (name.equals("planetNomer") && !(newPlanet == null)) {
					newPlanet.PlanetNomer = Integer.parseInt(parser.nextText());
					Log.w("myApp", "newPlanet.PlanetNomer " + newPlanet.PlanetNomer);
				} else if (name.equals("positionX") && !(newPlanet == null)) {
					newPlanet.PositionX = Integer.parseInt(parser.nextText());
					Log.w("myApp", "newPlanet.positionX " + newPlanet.PositionX);
				} else if (name.equals("positionY") && !(newPlanet == null)) {
					newPlanet.PositionY = Integer.parseInt(parser.nextText());
					Log.w("myApp", "newPlanet.positionY " + newPlanet.PositionY);
				} else if (name.equals("ships") && !(newPlanet == null)) {
					newPlanet.Ships = Integer.parseInt(parser.nextText());
					Log.w("myApp", "newPlanet.Ships " + newPlanet.Ships);
				} else if (name.equals("shipsPerTurn") && !(newPlanet == null)) {
					newPlanet.ShipsPerTurn = Integer.parseInt(parser.nextText());
					Log.w("myApp", "newPlanet.shipsPerTurn " + newPlanet.ShipsPerTurn);
				} else if (name.equals("attackRate") && !(newPlanet == null)) {
					newPlanet.AttackRate = Double.parseDouble(parser.nextText());
					Log.w("myApp", "newPlanet.AttackRate " + newPlanet.AttackRate);
				} else if (name.equals("playerId") && !(newPlanet == null)) {
					int PlayerID = Integer.parseInt(parser.nextText());
					Player PlanetPlayer = null;
					for (Player player : newGameMechanics.PlayerList.PlayerList) {
						if (player.PlayerId == PlayerID) {
							newPlanet.BelongsToPlayer = player;
							Log.w("myApp", "PlayerID " + PlayerID);
							break;
						}
					}
				}

				else if (name.equals("gameId")) {
					newGameMechanics.GameID = Integer.parseInt(parser.nextText());
					Log.w("myApp", "gameMechanics.GameID " + newGameMechanics.GameID);
				}
				else if (name.equals("pending")) {
					newGameMechanics.PendingGame = Boolean.parseBoolean(parser.nextText());
					Log.w("myApp", "gameMechanics.PendingGame " + newGameMechanics.PendingGame);
				} else if (name.equals("creator_userId")) {
					newGameMechanics.GameCreatorUserID = Integer.parseInt(parser.nextText());
					Log.w("myApp", "gameMechanics.GameCreatorUserID " + newGameMechanics.GameCreatorUserID);
				}
				break;
			case XmlPullParser.END_TAG:

				name = parser.getName();

				if (name.equals("player") && !(newPlayer == null)) {
					newGameMechanics.AddPlayer(newPlayer);
					Log.w("myApp", "AddPlayer");
					newPlayer = null;
				} else if (name.equals("planet") && !(newPlanet == null)) {
					newGameMechanics.AddPlanet(newPlanet);
					newPlanet = null;
					Log.w("myApp", "AddPlanet");
				}
				break;

			}
			eventType = parser.next();

		}

		gameMechanics.PlayerList = newGameMechanics.PlayerList;
		gameMechanics.planets = newGameMechanics.planets;

		gameMechanics.GameID = newGameMechanics.GameID;
		gameMechanics.PendingGame = newGameMechanics.PendingGame;
		gameMechanics.GameCreatorUserID = newGameMechanics.GameCreatorUserID;
	}

}
