package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.pes12.pickanevent.R;

public class BuscarEventoActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_buscar_evento);
        //EditText textoEvento = (EditText) findViewById(R.id.selectEvento);
        //textoEvento.setVisibility(View.INVISIBLE);
        //EditText textoLugar = (EditText) findViewById(R.id.selectLugar);
        //textoLugar.setVisibility(View.INVISIBLE);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.options_array
                ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id)
            {
                if(spinner.getSelectedItem().toString().equals("Evento")) {
                    EditText textoLugar = (EditText) findViewById(R.id.selectLugar);
                    textoLugar.setVisibility(View.INVISIBLE);
                    EditText textoEvento = (EditText) findViewById(R.id.selectEvento);
                    textoEvento.setVisibility(View.VISIBLE);
                 }
                else if(spinner.getSelectedItem().toString().equals("Lugar")) {
                    EditText textoLugar = (EditText) findViewById(R.id.selectLugar);
                    textoLugar.setVisibility(View.VISIBLE);
                    EditText textoEvento = (EditText) findViewById(R.id.selectEvento);
                    textoEvento.setVisibility(View.INVISIBLE);
                }
                else {
                    EditText textoLugar = (EditText) findViewById(R.id.selectLugar);
                    textoLugar.setVisibility(View.INVISIBLE);
                    EditText textoEvento = (EditText) findViewById(R.id.selectEvento);
                    textoEvento.setVisibility(View.INVISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
}
