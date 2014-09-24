package com.kotekhizanov.zconquest;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	/** Объект класса GameLoopThread */
	private GameThread mThread;

	public int shotX;
	public int shotY;
	
	int DeckWidth = 10;	    
    int DeckHeight = 10;
    int CeilSize = 100;
    int DeckBorder = 20;

    int TouchX;
    int TouchY;
    boolean Touching;
    
    float scale;
    
    Planets planets;

	/** Переменная запускающая поток рисования */
	private boolean running = false;

	// -------------Start of
	// GameThread--------------------------------------------------\\

	@SuppressLint("WrongCall")
	public class GameThread extends Thread {
		/** Объект класса */
		private GameView view;

		/** Конструктор класса */
		public GameThread(GameView view) {
			this.view = view;
		}

		/** Задание состояния потока */
		public void setRunning(boolean run) {
			running = run;
		}

		/** Действия, выполняемые в потоке */
		public void run() {
			while (running) {
				Canvas canvas = null;
				try {
					// подготовка Canvas-а
					canvas = view.getHolder().lockCanvas();
					synchronized (view.getHolder()) {
						// собственно рисование
						onDraw(canvas);
					}
				} catch (Exception e) {
				} finally {
					if (canvas != null) {
						view.getHolder().unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		
	}

	// -------------End of
	// GameThread--------------------------------------------------\\

	public GameView(Context context) {
		super(context);

		mThread = new GameThread(this);

		/* Рисуем все наши объекты и все все все */
		getHolder().addCallback(new SurfaceHolder.Callback() {
			/*** Уничтожение области рисования */
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				mThread.setRunning(false);
				while (retry) {
					try {
						// ожидание завершение потока
						mThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
			}

			/** Создание области рисования */
			public void surfaceCreated(SurfaceHolder holder) {
				mThread.setRunning(true);
				mThread.start();
			}

			/** Изменение области рисования */
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}
	
	public boolean onTouchEvent(MotionEvent e) 
    {
		TouchX = (int) (e.getX() - DeckBorder)/CeilSize + 1;
    	TouchY = (int) (e.getY() - DeckBorder)/CeilSize + 1;
    	
		switch (e.getAction())
		{
	    	
	        case MotionEvent.ACTION_DOWN:
	        	
	        	Touching = true;
	        	Log.w("myApp", "true");
	        	break;
	        	
	        case MotionEvent.ACTION_UP:

	        	Touching = false;
	        	
	        	if (TouchX < 1 || TouchX > DeckWidth ||
	        		TouchY < 1 || TouchY > DeckHeight)
	        	{
	        		return true;
	        	}
	        	
	        	planets.AddPlanet(TouchX, TouchY);

	        	Log.w("myApp", "false");
	        	break;
	        default:
                break;
		}    	
    	
        return true;
    }
	
	/** Функция рисующая все спрайты и фон */
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		 	Paint paint = new Paint();
		    paint.setStyle(Paint.Style.FILL);

		    // закрашиваем холст белым цветом
		    paint.setColor(Color.BLACK);
		    canvas.drawPaint(paint);
		    
		    
		    /*
		    for(int i=1; i<=DeckWidth; i++)
		    {
		    	for(int l=1; l<=DeckHeight; l++)
			    {
		    		if (Planets[i-1][l-1] == 1)
		    		{	    		
			    		// Рисуем желтый круг
			    	    paint.setAntiAlias(true);
			    	    paint.setColor(Color.YELLOW);
			    	    canvas.drawCircle(DeckBorder + i * CeilSize - CeilSize/2, DeckBorder + l * CeilSize - CeilSize/2, CeilSize/2 - 1, paint);
		    		}
			    }
		    }
		    */

		    paint.setColor(Color.GRAY);
		    for(int i=0; i<=DeckWidth; i++)
		    {
		    	canvas.drawRect(DeckBorder + i * CeilSize, DeckBorder, DeckBorder + i * CeilSize + 1, DeckBorder + CeilSize * DeckHeight, paint);
		    }

		    for(int i=0; i<=DeckHeight; i++)
		    {
		    	canvas.drawRect(DeckBorder, DeckBorder + i * CeilSize, DeckBorder + CeilSize * DeckWidth, DeckBorder + i * CeilSize + 1, paint);
		    }
		    
		    List<Planet> PlanetList = planets.GetPlanets();
		    Iterator <Planet> it = PlanetList.iterator(); 
		    while(it.hasNext())
		    {
		    	Planet p = it.next();

		    	paint.setAntiAlias(true);
	    	    paint.setColor(Color.YELLOW);
		    	canvas.drawCircle(DeckBorder + p.PositionX * CeilSize - CeilSize/2, DeckBorder + p.PositionY * CeilSize - CeilSize/2, CeilSize/2 - 1, paint);
		    	
		    	
		    	paint.setColor(Color.BLUE);
			    paint.setStyle(Paint.Style.FILL);
			    paint.setAntiAlias(true);
			    paint.setTextSize(30);
			    canvas.drawText(p.Name, DeckBorder + p.PositionX * CeilSize - CeilSize/2, DeckBorder + p.PositionY * CeilSize - CeilSize/2, paint);
			    
		    }
		    

		    if(Touching && !(TouchX < 1 || TouchX > DeckWidth || TouchY < 1 || TouchY > DeckHeight))
		    {
		    	Log.w("myApp", "TouchX = " + TouchX);
			    paint.setColor(Color.RED);
			    paint.setAlpha(70);
			    int TouchLineThickness = 3;
			    canvas.drawRect(DeckBorder + TouchX * CeilSize - CeilSize/2 - TouchLineThickness, DeckBorder, DeckBorder + TouchX * CeilSize - CeilSize/2 + TouchLineThickness + 1, DeckBorder + CeilSize * DeckHeight, paint);
			    canvas.drawRect(DeckBorder, DeckBorder + TouchY * CeilSize - CeilSize/2 - TouchLineThickness, DeckBorder + CeilSize * DeckWidth, DeckBorder + TouchY * CeilSize - CeilSize/2 + TouchLineThickness + 1, paint);
		    }
	}

}