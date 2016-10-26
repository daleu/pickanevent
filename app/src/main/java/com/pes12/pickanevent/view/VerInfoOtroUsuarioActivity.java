package com.pes12.pickanevent.view;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
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

    List<Info> grupos;
    List<Info> eventos;

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

        idUsuario = "-KUHeQd1dR1FT3FmbPLu";
        uMGR.getInfoUsuario(this, idUsuario);


        //gMGR = new GrupoMGR().getInstance();

        grupos = null; //llenar con todos los nombres de los grupos que sigue el ususario
        //Crear variable y añadirla al Adapter que contendra todas las imagenes de los grupos

        eventos = null;//Crear variable con los nombres de todos los eventos que siga el usuario
        //Crear variable con todas las fechas de los eventos que sigue el usuario
        //Crear varibale con todas las imagenes de los eventos que sigue el usuario

        ListAdapter adaptadorGrupos = new CustomAdapterGruposActivity(this, grupos);
        ListAdapter adaptadorEsdeveniments = new CustomAdapterEsdevenimentsActivity(this, eventos);

        ListView gruposUsuario = (ListView) findViewById(R.id.listaGruposUsuario);
        ListView esdevenimentsUsuario = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);

        gruposUsuario.setAdapter(adaptadorGrupos);
        esdevenimentsUsuario.setAdapter(adaptadorEsdeveniments);
    }

    public void mostrarInfoUsuario(UsuarioEntity usuario) {
        nombre.setText(usuario.getNickname());
        foto.setImageResource(R.drawable.oso); //Cambiar con imagen de usuario
        Map<String,Boolean> idGrupos = usuario.getIdGrupos();
        Map<String,Boolean> idEventos = usuario.getIdEventos();
        for (int i=0; i < idGrupos.size(); ++i){
            //rellenarListaGrupos();
        }
        for (int i=0; i < idEventos.size(); ++i){
            //rellenarListaEventos()
        }
    }

    public void rellenarListaGrupos (EventoEntity grupo){
        Info info = new Info(StringToBitMap(grupo.getImagen()),grupo.getTitulo(),null);
        grupos.add(info);
    }

    public void rellenarListaEventos (EventoEntity evento) {
        Info info = new Info(StringToBitMap(evento.getImagen()), evento.getTitulo(), evento.getHorario());
        eventos.add(info);
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}