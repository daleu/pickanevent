package com.pes12.pickanevent.view;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Info;

import java.util.List;

public class InfoOtroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_otro_usuario);



        List<Info> grupos = null; //llenar con todos los nombres de los grupos que sigue el ususario
        //Crear variable y añadirla al Adapter que contendra todas las imagenes de los grupos

        List<Info> esdeveniments = null;//Crear variable con los nombres de todos los eventos que siga el usuario
        //Crear variable con todas las fechas de los eventos que sigue el usuario
        //Crear varibale con todas las imagenes de los eventos que sigue el usuario

        ListAdapter adaptadorGrupos = new CustomAdapterGrupos(this, grupos);
        ListAdapter adaptadorEsdeveniments = new CustomAdapterEsdeveniments(this, esdeveniments);

        ListView gruposUsuario = (ListView) findViewById(R.id.listaGruposUsuario);
        ListView esdevenimentsUsuario = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);

        gruposUsuario.setAdapter(adaptadorGrupos);
        esdevenimentsUsuario.setAdapter(adaptadorEsdeveniments);
    }
}