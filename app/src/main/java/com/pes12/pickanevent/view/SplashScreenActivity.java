package com.pes12.pickanevent.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pes12.pickanevent.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        //LA REDIRECCIÓN REAL SERA LA COMENTADA
        //Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
