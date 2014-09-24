package com.kotekhizanov.zconquest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Draw2D extends View{
	
	int DeckWidth = 10;	    
    int DeckHeight = 10;
    int CeilSize = 100;
    int DeckBorder = 20;
    
    float scale;
    
    int [][] Planets;
	
    public Draw2D(Context context)
    {
    	super(context);
    	//
    	/*
    	DisplayMetrics metrics = new DisplayMetrics();    
    	WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int DisplayWidth = windowManager.getDefaultDisplay().getWidth();
                
        CeilSize = (int) ((DisplayWidth - DeckBorder*2)/DeckWidth);
        DeckBorder = (int) (DisplayWidth - CeilSize * DeckWidth) / 2;
        */
    	
    	Planets = new int[DeckWidth][DeckHeight];
        
        for(int i=0; i<DeckWidth; i++)
        {
        	for(int l=0; l<DeckHeight; l++)
    	    {
        		Planets[i][l] = 0;
    	    }
        }
        
        

        
        Planets[1][3] = 1;
        Planets[8][9] = 1;
        
        
	}
    
    
    
    
    
	@Override
	protected void onDraw(Canvas c){
	    super.onDraw(c);
	    
	    	    
	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL);

	    // закрашиваем холст белым цветом
	    paint.setColor(Color.BLACK);
	    c.drawPaint(paint);
	    
	    
	    for(int i=1; i<=DeckWidth; i++)
	    {
	    	for(int l=1; l<=DeckHeight; l++)
		    {
	    		if (Planets[i-1][l-1] == 1)
	    		{	    		
		    		// Рисуем желтый круг
		    	    paint.setAntiAlias(true);
		    	    paint.setColor(Color.YELLOW);
		    	    c.drawCircle(DeckBorder + i * CeilSize - CeilSize/2, DeckBorder + l * CeilSize - CeilSize/2, CeilSize/2 - 1, paint);
	    		}
		    }
	    }
	    

	    
	    paint.setColor(Color.GRAY);
	    for(int i=0; i<=DeckWidth; i++)
	    {
		    c.drawRect(DeckBorder + i * CeilSize, DeckBorder, DeckBorder + i * CeilSize + 1, DeckBorder + CeilSize * DeckHeight, paint);
	    }

	    for(int i=0; i<=DeckHeight; i++)
	    {
		    c.drawRect(DeckBorder, DeckBorder + i * CeilSize, DeckBorder + CeilSize * DeckWidth, DeckBorder + i * CeilSize + 1, paint);
	    }
	    
	    
	 // Рисуем текст
	    paint.setColor(Color.BLUE);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setAntiAlias(true);
	    paint.setTextSize(30);
	    c.drawText("scale" + scale, 30, 200, paint);
	    c.drawText("CeilSize" + CeilSize, 30, 300, paint);
	    
	    
	    /*
	    // Рисуем зеленый прямоугольник
	    paint.setColor(Color.GREEN);
	    c.drawRect(20, 200, 460, 230, paint);
	    
	    // Рисуем текст
	    paint.setColor(Color.BLUE);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setAntiAlias(true);
	    paint.setTextSize(30);
	    c.drawText("Лужайка для котов", 30, 200, paint);
	    
	    // Текст под углом
	    int x = 310;
	    int y = 190;
	     
	    paint.setColor(Color.GRAY);
	    paint.setTextSize(25);
	    String str2rotate = "Лучик солнца!";

	    // Создаем ограничивающий прямоугольник для наклонного текста
	    Rect rect = new Rect();

	    // поворачиваем холст по центру текста
	    c.rotate(-45, x + rect.exactCenterX(), y + rect.exactCenterY());

	    // Рисуем текст
	    paint.setStyle(Paint.Style.FILL);
	    c.drawText(str2rotate, x, y, paint);
	    */

	    // восстанавливаем холст
	    c.restore();
	    
	    // Выводим значок из ресурсов
	    /*
	    Resources res = this.getResources();
	    Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.icon);
	    c.drawBitmap(bitmap, 415, 655, paint);
	    */
	}
}