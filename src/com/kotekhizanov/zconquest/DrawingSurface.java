package com.kotekhizanov.zconquest;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DrawingSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	GameMechanics gameMechanics;
	
	Context context;
	SurfaceHolder mHolder;

	public MyThread mythread;

	public int shotX;
	public int shotY;

	int DeckWidth = 10;
	int DeckHeight = 10;
	int CeilSize = 50;
	int DeckBorder = 20;

	int TouchX;
	int TouchY;
	boolean Touching;

	float scale;

	Planet EditablePLanet;
	int EditablePLanetStartPosX;
	int EditablePLanetStartPosY;
	
	boolean SendingShips;
	Planet SendingStartPlanet;
	Planet SendingEndPlanet;
	
	GameActivity gameActivity;
	
	Button EndTurnButton;
	TextView SelectedPLanetText;
	TextView SelectedPLanet;
	TextView PlanetInfoShips;
	TextView PlanetInfoIncome;
	TextView PlanetInfoAttackRate;
	TextView SendShipsText;
	TextView SendShipsAmount;
	Button SendShipsSend;

	public DrawingSurface(Context context, GameActivity gameActivity) {
		super(context);
		this.context = context;
		this.gameActivity = gameActivity;
		init();
		
		EndTurnButton = (Button) findViewById(R.id.EndTurnButton);
		SelectedPLanetText = (TextView) findViewById(R.id.SelectedPLanetText);
		SelectedPLanet = (TextView) findViewById(R.id.SelectedPLanet);
		PlanetInfoShips = (TextView) findViewById(R.id.PlanetInfoShips);
		PlanetInfoIncome = (TextView) findViewById(R.id.PlanetInfoIncome);
		PlanetInfoAttackRate = (TextView) findViewById(R.id.PlanetInfoAttackRate);
		SendShipsText = (TextView) findViewById(R.id.SendShipsText);
		SendShipsAmount = (TextView) findViewById(R.id.SendShipsAmount);
		SendShipsSend = (Button) findViewById(R.id.SendShipsSend);
		
		gameMechanics = GameMechanics.get(context);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		CeilSize = (int) Math.floor((metrics.widthPixels - DeckBorder * 2) / DeckWidth);
	}

	private void init() {
		mHolder = getHolder();
		mHolder.addCallback(this);

	}

	int lastX, lastY, currX, currY;
	boolean isDeleting;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		TouchX = (int) (e.getX() - DeckBorder) / CeilSize + 1;
		TouchY = (int) (e.getY() - DeckBorder) / CeilSize + 1;
		
		if (TouchX < 1 || TouchX > DeckWidth || TouchY < 1
				|| TouchY > DeckHeight) {
			return true;
		}
		
		Planet PlanetOnPosition = gameMechanics.planets.GetPlanet(TouchX, TouchY);
		boolean FreeToDeployPlanet = (PlanetOnPosition == null || PlanetOnPosition == EditablePLanet);

		switch (e.getAction()) {

		case MotionEvent.ACTION_DOWN:

			
			
			if(gameMechanics.PendingGame)
			{
				EditablePLanet = gameMechanics.planets.GetPlanet(TouchX, TouchY);
				
				if(!(EditablePLanet == null))
				{				
					gameMechanics.PlanetPositionEdit = true;
					EditablePLanetStartPosX = EditablePLanet.PositionX;
					EditablePLanetStartPosY = EditablePLanet.PositionY;
				}
			}
			else
			{
				gameMechanics.SelectedPLanet = gameMechanics.planets.GetPlanet(TouchX, TouchY);
				SendingStartPlanet = gameMechanics.SelectedPLanet; 
				if(!(!(SendingStartPlanet == null) && !(SendingStartPlanet.BelongsToPlayer == null) && SendingStartPlanet.BelongsToPlayer.PlayerId == gameMechanics.CurrentPlayer.PlayerId))
				{
					SendingStartPlanet = null;
				}
				SendingEndPlanet = null;
				
				ShowSelectedPlanetInfo();
			}
			Touching = true;
			Log.w("myApp", "true");
			break;

		case MotionEvent.ACTION_UP:

			
			if(gameMechanics.PendingGame && gameMechanics.PlanetPositionEdit && !(EditablePLanet == null) && FreeToDeployPlanet)
			{
				if (!(TouchX < 1 || TouchX > DeckWidth || TouchY < 1
						|| TouchY > DeckHeight)) {
					EditablePLanet.PositionX = TouchX;
					EditablePLanet.PositionY = TouchY;
				}else
				{
					EditablePLanet.PositionX = EditablePLanetStartPosX;
					EditablePLanet.PositionY = EditablePLanetStartPosY;
				}
			}
			else
			{
				SendingEndPlanet = gameMechanics.planets.GetPlanet(TouchX, TouchY);
				if(SendingEndPlanet == null)
				{
					SendingStartPlanet = null;
				}
			}
			
			gameMechanics.PlanetPositionEdit = false;
			Touching = false;

			//gameMechanics.AddPlanet(TouchX, TouchY);

			Log.w("myApp", "false");
			break;
			
		case MotionEvent.ACTION_MOVE:

			if(gameMechanics.PendingGame && gameMechanics.PlanetPositionEdit && !(EditablePLanet == null) && FreeToDeployPlanet)
			{
				if (!(TouchX < 1 || TouchX > DeckWidth || TouchY < 1
						|| TouchY > DeckHeight)) {
					EditablePLanet.PositionX = TouchX;
					EditablePLanet.PositionY = TouchY;
				}else
				{
					EditablePLanet.PositionX = EditablePLanetStartPosX;
					EditablePLanet.PositionY = EditablePLanetStartPosY;
				}
			}else
			{
				/*
				if(SendingStartPlanet == null)
				{					
					SendingStartPlanet = gameMechanics.planets.GetPlanet(TouchX, TouchY);
				}
				*/
			}

			//gameMechanics.AddPlanet(TouchX, TouchY);

			Log.w("myApp", "false");
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		mythread = new MyThread(holder, context, this);
		
		mythread.setRunning(true);
		mythread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		mythread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				mythread.join();
				retry = false;
			} catch (Exception e) {
				Log.v("Exception Occured", e.getMessage());
			}
		}
	}
	
	
	
	public void ShowSelectedPlanetInfo()
	{
		if(!(gameMechanics.SelectedPLanet == null))
		{
			this.gameActivity.PlanetInfoShips.setText("Ships " + gameMechanics.SelectedPLanet.Ships);
			this.gameActivity.PlanetInfoIncome.setText("Income " + gameMechanics.SelectedPLanet.ShipsPerTurn);
			this.gameActivity.PlanetInfoAttackRate.setText("Attack rate " + gameMechanics.SelectedPLanet.AttackRate);
			
			if(gameMechanics.SelectedPLanet.BelongsToPlayer == gameMechanics.CurrentPlayer)
			{
				this.gameActivity.SendShipsGroupRL.setVisibility(View.VISIBLE);
			}
			else
			{
				this.gameActivity.SendShipsGroupRL.setVisibility(View.INVISIBLE);
			}
			
			
		}
		else
		{
			this.gameActivity.SendShipsGroupRL.setVisibility(View.INVISIBLE);
			this.gameActivity.SendShipsAmount.setText("");
		}
		
	}
	
	
	protected void doDraw(Canvas canvas) {

		canvas.drawColor(Color.BLACK);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);

		// закрашиваем холст белым цветом
		paint.setColor(Color.BLACK);
		canvas.drawPaint(paint);

		paint.setColor(Color.GRAY);
		for (int i = 0; i <= DeckWidth; i++) {
			canvas.drawRect(DeckBorder + i * CeilSize, DeckBorder, DeckBorder
					+ i * CeilSize + 1, DeckBorder + CeilSize * DeckHeight,
					paint);
		}

		for (int i = 0; i <= DeckHeight; i++) {
			canvas.drawRect(DeckBorder, DeckBorder + i * CeilSize, DeckBorder
					+ CeilSize * DeckWidth, DeckBorder + i * CeilSize + 1,
					paint);
		}

		List<Planet> PlanetList = gameMechanics.planets.GetPlanets();
		Iterator<Planet> it = PlanetList.iterator();
		while (it.hasNext()) {
			Planet p = it.next();

			if(!(p.BelongsToPlayer == null))
			{
				int LineThickness = CeilSize/2-1;
				int margin = 1;
				
				//paint.setColor(p.BelongsToPlayer.PlayerColor);
				paint.setColor(gameMechanics.PlayerList.GetPlayerColor(p.BelongsToPlayer));
				paint.setAlpha(70);
				//top
				canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
								DeckBorder + (p.PositionX + 0) * CeilSize - margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin + LineThickness, paint);
				
				//bottom
				canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY) * CeilSize - margin + 1 - LineThickness ,
								DeckBorder + (p.PositionX + 0) * CeilSize - margin, DeckBorder + (p.PositionY) * CeilSize - margin + 1, paint);
				
				//left
				canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
								DeckBorder + (p.PositionX - 1) * CeilSize + margin + LineThickness, DeckBorder + (p.PositionY) * CeilSize, paint);
				
				//right
				canvas.drawRect(DeckBorder + (p.PositionX) * CeilSize - margin + 1 - LineThickness, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
								DeckBorder + (p.PositionX) * CeilSize - margin + 1, DeckBorder + (p.PositionY) * CeilSize, paint);
			}
			
			paint.setAntiAlias(true);
			paint.setAlpha(100);
			paint.setColor(Color.DKGRAY);
			canvas.drawCircle(DeckBorder + p.PositionX * CeilSize - CeilSize
					/ 2, DeckBorder + p.PositionY * CeilSize - CeilSize / 2,
					CeilSize / 2 - 3, paint);
			paint.setColor(p.DefaultColor);
			canvas.drawCircle(DeckBorder + p.PositionX * CeilSize - CeilSize
					/ 2, DeckBorder + p.PositionY * CeilSize - CeilSize / 2,
					CeilSize / 2 - 6, paint);

			
			paint.setColor(Color.DKGRAY);
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setTextSize((int) Math.floor((((double)CeilSize)/100) * 30));
			canvas.drawText(p.Name, DeckBorder + p.PositionX * CeilSize - CeilSize / 2 + (int) Math.floor((((double)CeilSize)/100) * 10), DeckBorder + p.PositionY * CeilSize - CeilSize / 2 - (int) Math.floor((((double)CeilSize)/100) * 10), paint);
			

		}
		
		
		
		List<Player> PlayerList = gameMechanics.PlayerList.PlayerList;
		Iterator<Player>playerIt = PlayerList.iterator();
		int nomer = 0;
		while (playerIt.hasNext()) {
			Player player = playerIt.next();

			paint.setAntiAlias(true);
			paint.setColor(gameMechanics.PlayerList.GetPlayerColor(player));
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setTextSize(60);
			canvas.drawText(player.PlayerName, 700, DeckBorder + DeckHeight * CeilSize + 100 + 60*nomer, paint);
			
			
			nomer++;

		}

		if (Touching
				&& !(TouchX < 1 || TouchX > DeckWidth || TouchY < 1 || TouchY > DeckHeight)) {
			paint.setColor(Color.RED);
			paint.setAlpha(90);
			int TouchLineThickness = 3;
			canvas.drawRect(DeckBorder + TouchX * CeilSize - CeilSize / 2
					- TouchLineThickness, DeckBorder, DeckBorder + TouchX
					* CeilSize - CeilSize / 2 + TouchLineThickness + 1,
					DeckBorder + CeilSize * DeckHeight, paint);
			canvas.drawRect(DeckBorder, DeckBorder + TouchY * CeilSize
					- CeilSize / 2 - TouchLineThickness, DeckBorder + CeilSize
					* DeckWidth, DeckBorder + TouchY * CeilSize - CeilSize / 2
					+ TouchLineThickness + 1, paint);
		}
		
		if(!(gameMechanics.SelectedPLanet == null))
		{
			int LineThickness = 3;
			int margin = 0;
			Planet p = gameMechanics.SelectedPLanet;
			
			//paint.setColor(p.BelongsToPlayer.PlayerColor);
			paint.setColor(Color.WHITE);
			//top
			canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
							DeckBorder + (p.PositionX + 0) * CeilSize - margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin + LineThickness, paint);
			
			//bottom
			canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY) * CeilSize - margin + 1 - LineThickness ,
							DeckBorder + (p.PositionX + 0) * CeilSize - margin, DeckBorder + (p.PositionY) * CeilSize - margin + 1, paint);
			
			//left
			canvas.drawRect(DeckBorder + (p.PositionX - 1) * CeilSize + margin, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
							DeckBorder + (p.PositionX - 1) * CeilSize + margin + LineThickness, DeckBorder + (p.PositionY) * CeilSize, paint);
			
			//right
			canvas.drawRect(DeckBorder + (p.PositionX) * CeilSize - margin + 1 - LineThickness, DeckBorder + (p.PositionY - 1) * CeilSize + margin ,
							DeckBorder + (p.PositionX) * CeilSize - margin + 1, DeckBorder + (p.PositionY) * CeilSize, paint);
		}
		
		if(!(SendingStartPlanet == null || SendingEndPlanet == null))
		{
			paint.setColor(SendingStartPlanet.BelongsToPlayer.PlayerColor);
			paint.setStrokeWidth(3);
			canvas.drawLine(DeckBorder + SendingStartPlanet.PositionX * CeilSize - CeilSize / 2, DeckBorder + SendingStartPlanet.PositionY * CeilSize - CeilSize / 2, 
					DeckBorder + SendingEndPlanet.PositionX * CeilSize - CeilSize / 2,
					DeckBorder + SendingEndPlanet.PositionY * CeilSize - CeilSize / 2, paint);
			
		}
		
		

		// canvas.drawBitmap(backBuffer, 0, 0, paint);

	}

}
