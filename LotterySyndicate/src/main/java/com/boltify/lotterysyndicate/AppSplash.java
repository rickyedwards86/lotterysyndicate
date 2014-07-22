package com.boltify.lotterysyndicate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Created by RickEdwards on 20/03/14.
 */
public class AppSplash extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity();
                //Intent i = new Intent(BoltifySplash.this, AppSplash.class);
                //startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startActivity()
    {
        Intent i = new Intent(AppSplash.this, MainActivity.class);
        startActivity(i);
    }
}
