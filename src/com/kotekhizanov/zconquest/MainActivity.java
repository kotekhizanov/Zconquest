package com.kotekhizanov.zconquest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        /*
        Draw2D d = new Draw2D(this);
        setContentView(d);
        */

        setContentView(new GameView(this));
    }
}