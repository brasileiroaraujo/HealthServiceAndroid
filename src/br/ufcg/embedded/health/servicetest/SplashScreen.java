package br.ufcg.embedded.health.servicetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import br.ufcg.embedded.health.R;

public class SplashScreen extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler h = new Handler();
        h.postDelayed(this, 3000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, HealthServiceTestActivity.class));
        finish();
    }
}
