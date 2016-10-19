package com.pes12.pickanevent.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;

public class VerInfoGrupo extends AppCompatActivity {

    TextView descripcionTV;
    LinearLayout events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_grupo);
        descripcionTV = (TextView)findViewById(R.id.descripcion);
        descripcionTV.setMovementMethod(new ScrollingMovementMethod());

        events = (LinearLayout)findViewById(R.id.ev);
    }
}
