package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;

public class SplashScreenActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // intent = new Intent(this, MainActivity.class);
        //LA REDIRECCIÓN REAL SERA LA COMENTADA



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent;
                if (getAuth().getCurrentUser() == null) {
                    setUsuarioActual(null);
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                else {
                    intent = new Intent(SplashScreenActivity.this, NavigationDrawer.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
