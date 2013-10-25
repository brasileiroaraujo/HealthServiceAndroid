package com.signove.health.servicetest;

import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.content.Intent;
public class SplashScreen extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler h = new Handler();
        h.postDelayed(this, 3000);
    }

    public void run() {
        startActivity(new Intent(this, HealthServiceTestActivity.class));
        finish();
    }
}


