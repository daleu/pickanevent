package com.pes12.pickanevent.view;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pes12.pickanevent.R;

public class VerInfoEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_evento);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TextView clockIcon = (TextView) findViewById(R.id.clockIcon);
        clockIcon.setTypeface(fontAwesomeFont);
        TextView clockDollar = (TextView) findViewById(R.id.clockDollar);
        clockDollar.setTypeface(fontAwesomeFont);
    }
}
