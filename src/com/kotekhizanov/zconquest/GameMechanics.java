package com.kotekhizanov.zconquest;

import android.content.Context;

public class GameMechanics {

	public static GameMechanics sGameMechanics;
	public Context mAppContext;

	public String ServerUrl;
	public int GameID;
	public boolean PendingGame;

	public Players PlayerList;
	public Player CurrentPlayer;
	public int GameCreatorUserID;
	
	public Planets planets;
	public boolean PlanetPositionEdit;
	public boolean StartPlanetPositionEdit;
	public Planet SelectedPLanet;

	public GameMechanics(Context mAppContext) {
		this.mAppContext = mAppContext;
		ServerUrl = "http://kotetest.ilc.ge/";
		
		planets = new Planets();
		PlayerList = new Players();
		
		planets = new Planets();
	}

	public static GameMechanics get(Context c) {
		if (sGameMechanics == null) {
			sGameMechanics = new GameMechanics(c.getApplicationContext());
		}

		return sGameMechanics;
	}

	public void AddPlanet(int positionX, int positionY) {
		
		planets.AddPlanet(positionX, positionY);
		
	}

	public void AddPlanet(Planet newPlanet) {
		
		planets.AddPlanet(newPlanet);
		
	}
	


	public void AddPlayer(Player newPlayer) {
		
		PlayerList.AddPlayer(newPlayer);
		
	}

}
