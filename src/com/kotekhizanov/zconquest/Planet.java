package com.kotekhizanov.zconquest;

import android.graphics.Color;


public class Planet {
	
	int PositionX;
	int PositionY;
	int PlanetNomer;
	int Ships;
	
	int ShipsPerTurn;
	double AttackRate;
	
	public Player BelongsToPlayer;
	public int DefaultColor;
	
	String Name;

	public Planet(int x, int y, String PlanetName)
	{
		PositionX = x;
		PositionY = y;
		Name = PlanetName;
		
		DefaultColor = Color.LTGRAY;
		
		BelongsToPlayer = null;
	}
	
	public Planet() {
		DefaultColor = Color.LTGRAY;
	}

	public void SetPlayer(Player player)
	{
		BelongsToPlayer = player;
	}

	/*
	public void onDraw()
	{
		Paint paint = new Paint();
		
	    paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
	    paint.setColor(Color.YELLOW);
	    canvas.drawCircle(DeckBorder + i * CeilSize - CeilSize/2, DeckBorder + l * CeilSize - CeilSize/2, CeilSize/2 - 1, paint);
	}	
	*/

}
