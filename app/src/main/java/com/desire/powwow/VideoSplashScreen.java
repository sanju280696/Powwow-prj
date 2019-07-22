package com.desire.powwow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/*
public class VideoSplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private void jump() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                */
/* Create an Intent that will start the Menu-Activity. *//*

                Intent mainIntent = new Intent(VideoSplashScreen.this, Login.class);
                VideoSplashScreen.this.startActivity(mainIntent);
                VideoSplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Utils.checkConnection(this)) {
            // Not Available...
            Snackbar.make(findViewById(android.R.id.content), "Please Connect to Internet", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }

        jump();
    }
}
*/
