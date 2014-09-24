package com.kotekhizanov.zconquest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planets {

	private List<Planet> PlanetList;
	private List<String> PlanetNames;

	public Planets()
	{
		PlanetList = new ArrayList<Planet>();
		PlanetNames = new ArrayList<String>();

		PlanetNames.add("A");
		PlanetNames.add("B");
		PlanetNames.add("C");
		PlanetNames.add("D");
		PlanetNames.add("E");
		PlanetNames.add("F");
		PlanetNames.add("G");
		PlanetNames.add("H");
		PlanetNames.add("I");
		PlanetNames.add("J");
		PlanetNames.add("K");
		PlanetNames.add("L");
		PlanetNames.add("M");
		PlanetNames.add("N");
		PlanetNames.add("O");
		PlanetNames.add("P");
		PlanetNames.add("Q");
		PlanetNames.add("R");
		PlanetNames.add("S");
		PlanetNames.add("T");
		PlanetNames.add("U");
		PlanetNames.add("V");
		PlanetNames.add("W");
		PlanetNames.add("X");
		PlanetNames.add("Y");
		PlanetNames.add("Z");
	}
	
	public boolean AddPlanet(int x, int y)
	{
		if(GetPlanet(x, y) == null)
		{
			String planetNameNomer = "";
			if((int)PlanetList.size() / PlanetNames.size() == 0)
			{
				planetNameNomer = "";
			}
			else
			{
				planetNameNomer = "" + (int)PlanetList.size() / PlanetNames.size();
			}
			
			PlanetList.add(new Planet(x, y, (String)PlanetNames.get(PlanetList.size() % PlanetNames.size()) + planetNameNomer));
			return true;
		}
		
		return false;
		
	}
	
	public void AddPlanet(Planet newPlanet) {
		
		String planetNameNomer = "";
		if((int)PlanetList.size() / PlanetNames.size() == 0)
		{
			planetNameNomer = "";
		}
		else
		{
			planetNameNomer = "" + (int)PlanetList.size() / PlanetNames.size();
		}
		
		newPlanet.Name = (String)PlanetNames.get(PlanetList.size() % PlanetNames.size()) + planetNameNomer;
		PlanetList.add(newPlanet);		
	}
	
	public void DeleteAllPlanets() {
		PlanetList = new ArrayList<Planet>();
	}
	
	public Planet GetPlanet(int x, int y)
	{
		for(Planet planet: PlanetList)
		{
			if(planet.PositionX == x && planet.PositionY == y)
			{
				return planet;
			}
		}
		
		return null;
	}
	
	public Planet GetNPCRandomPlanet()
	{
		Planet FirstPlanet = PlanetList.get(0);
		return FirstPlanet;
		/*
		for(Planet planet: PlanetList)
		{
			return planet;
		}
		return null;
		*/
		
		
	}
	
	public List<Planet> GetPlanets()
	{
		return PlanetList;
	}

	

}
