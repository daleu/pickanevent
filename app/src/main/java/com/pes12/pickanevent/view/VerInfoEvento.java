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

        //Posar iconos
        TextView clockIcon = (TextView) findViewById(R.id.clockIcon);
        clockIcon.setTypeface(fontAwesomeFont);
        TextView dollarIcon = (TextView) findViewById(R.id.clockDollar);
        dollarIcon.setTypeface(fontAwesomeFont);
        TextView pinIcon = (TextView) findViewById(R.id.mapIcon);
        pinIcon.setTypeface(fontAwesomeFont);

    }
}
