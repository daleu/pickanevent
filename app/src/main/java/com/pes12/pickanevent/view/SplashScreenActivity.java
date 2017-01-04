package com.pes12.pickanevent.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pes12.pickanevent.R;

public class SplashScreenActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        //LA REDIRECCIÓN REAL SERA LA COMENTADA
        /*if (getUsuarioActual() == null) {
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, NavigationDrawer.class);
        }*/
        startActivity(intent);
        finish();
    }
}
