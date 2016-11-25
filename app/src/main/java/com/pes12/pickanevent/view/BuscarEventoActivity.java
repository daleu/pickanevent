package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.util.ArrayList;

public class BuscarEventoActivity extends BaseActivity {

    EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_buscar_evento);
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
    public void buscarEvento(View _view) {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        if(spinner.getSelectedItem().toString().equals("Evento")) {
            EditText textoEvento = (EditText) findViewById(R.id.selectEvento);
            if (textoEvento.getText().toString().equals("")) Toast.makeText(this,R.string.ERROR,Toast.LENGTH_LONG).show();
            else eMGR.getInfoEventoElegido(this,"titulo",textoEvento.getText().toString());
        }
        if(spinner.getSelectedItem().toString().equals("Lugar")) {
            EditText textoLugar = (EditText) findViewById(R.id.selectLugar);
            if (textoLugar.getText().toString().equals("")) Toast.makeText(this,R.string.ERROR,Toast.LENGTH_LONG).show();
            else eMGR.getInfoEventoElegido(this,"localizacion",textoLugar.getText().toString());
        }
    }
    public void mostrarInfoEventoElegido(ArrayList<EventoEntity> _eventos) {
        if (_eventos.isEmpty()) Toast.makeText(this,R.string.EVENTO_NO_ENCONTRADO,Toast.LENGTH_LONG).show();
        else {
            for (int i = 0; i < _eventos.size(); ++i) {
                Toast.makeText(this, _eventos.get(i).getTitulo(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
