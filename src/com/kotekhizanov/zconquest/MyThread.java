package com.kotekhizanov.zconquest;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MyThread extends Thread {
	boolean mRun;
	Canvas mcanvas;
	SurfaceHolder surfaceHolder;
	Context context;
	DrawingSurface mDrawingSurface;

	public MyThread(SurfaceHolder sholder, Context ctx, DrawingSurface spanel) {
		surfaceHolder = sholder;
		context = ctx;
		mRun = false;
		mDrawingSurface = spanel;
	}

	public void setRunning(boolean bRun) {
		mRun = bRun;
	}

	@Override
	public void run() {
		try {
			super.run();
		} catch (Exception e) {
			// TODO: handle exception
		}
		//while (true) {
		while (mRun) {
			if (mRun) {
				mcanvas = surfaceHolder.lockCanvas();
				if (mcanvas != null) {
					mDrawingSurface.doDraw(mcanvas);
					surfaceHolder.unlockCanvasAndPost(mcanvas);
				}
			}
			else
			{
				mcanvas = surfaceHolder.lockCanvas();
				if (mcanvas != null) {
					surfaceHolder.unlockCanvasAndPost(mcanvas);
				}
			}

		}
	}
}