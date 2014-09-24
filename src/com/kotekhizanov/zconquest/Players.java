package com.kotekhizanov.zconquest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

public class Players {

	public List<Player> PlayerList;
	private List<Integer> ColorList;

	public Players()
	{
		PlayerList = new ArrayList<Player>();
		ColorList = new ArrayList<Integer>();
		ColorList.add(Color.RED);
		ColorList.add(Color.BLUE);
		ColorList.add(Color.YELLOW);
		ColorList.add(Color.CYAN);
		ColorList.add(Color.GREEN);
	}
	
	public boolean AddPlayer(int playerId, String playerName)
	{
		PlayerList.add(new Player(playerId, playerName));
		return true;
	}
	
	public boolean AddPlayer(Player newPlayer)
	{
		PlayerList.add(newPlayer);
		return true;
	}
	
	public List<Player> GetPlayers()
	{
		return PlayerList;
	}
	
	public void LogPlayers()
	{
		Iterator<Player> it = PlayerList.iterator();
		while(it.hasNext())
		{
			Player currPlayer  = it.next();
			Log.w("myApp", "id = " + currPlayer.PlayerId + " Name = " + currPlayer.PlayerName);

		}
	}
	
	public int GetPlayerColor(Player player)
	{
		int nomer = 0;
		Iterator<Player> it = PlayerList.iterator();
		while(it.hasNext())
		{
			Player currPlayer = it.next();
			
			if(player == currPlayer)
			{
				return ColorList.get(nomer);
			}
			
			nomer++;
			
		}
		return 0;
	}


}
