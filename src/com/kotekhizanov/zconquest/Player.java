package com.kotekhizanov.zconquest;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;


public class Player {
	
	public int PlayerId;
	public String PlayerName;
	
	public int PlayerColor;
	
	public List<Planet> Planets;
	

	public Player() {
		super();
		
		PlayerColor = Color.RED;		
		Planets = new ArrayList<Planet>();
	}


	public Player(int playerId, String playerName)
	{
		PlayerId = playerId;
		PlayerName = playerName;
		
		
	}
	
	public void addPlanet(Planet planet)
	{
		Planets.add(planet);
		planet.SetPlayer(this);
	}
	
	
	

}
