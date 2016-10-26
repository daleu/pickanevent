package com.pes12.pickanevent.view;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.List;
import java.util.Map;

public class VerInfoOtroUsuarioActivity extends BaseActivity {

    TextView nombre;
    ImageView foto;
    ListView listaGrupos;
    ListView listaEventos;

    String idUsuario;
    String idGrupo;
    String idEvento;

    UsuarioMGR uMGR;
    GrupoMGR gMGR;
    EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_otro_usuario);

        //Inicializaciones
        nombre = (TextView)findViewById(R.id.textNombreUsuario);
        foto = (ImageView)findViewById(R.id.imagenOtroUsuario);
        listaGrupos = (ListView) findViewById(R.id.listaGruposUsuario);
        listaEventos = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);
        ///////////////////////////////////////////////////////////////////////

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        eMGR = MGRFactory.getInstance().getEventoMGR();

        idUsuario = "Aser2";
        uMGR.getUsersByUsername(this, idUsuario);


        //gMGR = new GrupoMGR().getInstance();

        //List<Info> grupos = null; //llenar con todos los nombres de los grupos que sigue el ususario
        //Crear variable y añadirla al Adapter que contendra todas las imagenes de los grupos

        //List<Info> esdeveniments = null;//Crear variable con los nombres de todos los eventos que siga el usuario
        //Crear variable con todas las fechas de los eventos que sigue el usuario
        //Crear varibale con todas las imagenes de los eventos que sigue el usuario

       // ListAdapter adaptadorGrupos = new CustomAdapterGruposActivity(this, grupos);
        //ListAdapter adaptadorEsdeveniments = new CustomAdapterEsdevenimentsActivity(this, esdeveniments);

        //ListView gruposUsuario = (ListView) findViewById(R.id.listaGruposUsuario);
        //ListView esdevenimentsUsuario = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);

        //gruposUsuario.setAdapter(adaptadorGrupos);
        //esdevenimentsUsuario.setAdapter(adaptadorEsdeveniments);
    }

    public void infoUsuario(Map<String,UsuarioEntity> hm) {

        System.out.println("Mostrando los valores:");
        nombre.setText("");

        for (Map.Entry<String, UsuarioEntity> entry : hm.entrySet()) {
            nombre.setText(nombre.getText()+ "\r\n"+entry.getValue().getNickname());
            System.out.println("clave=" + entry.getKey() + ", nickanme=" + entry.getValue().toString());
        }
    }

}