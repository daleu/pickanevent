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
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
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

    ArrayList<Info> grupos;
    ArrayList<Info> eventos;

    UsuarioMGR uMGR;
    GrupoMGR gMGR;
    EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_otro_usuario);

        //Progres dialog
        showProgressDialog();

        //Inicializaciones
        nombre = (TextView)findViewById(R.id.textNombreUsuario);
        foto = (ImageView)findViewById(R.id.imagenOtroUsuario);
        listaGrupos = (ListView) findViewById(R.id.listaGruposUsuario);
        listaEventos = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);
        grupos = new ArrayList<Info>();
        eventos= new ArrayList<Info>();
        ///////////////////////////////////////////////////////////////////////

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        eMGR = MGRFactory.getInstance().getEventoMGR();

        idUsuario = "-KUHeQd1dR1FT3FmbPLu";
        uMGR.getInfoUsuario(this, idUsuario);

        //ListAdapter adaptadorGrupos = new CustomAdapterGruposActivity(this, grupos);
        //ListAdapter adaptadorEsdeveniments = new CustomAdapterEsdevenimentsActivity(this, eventos);

        //ListView gruposUsuario = (ListView) findViewById(R.id.listaGruposUsuario);
        //ListView esdevenimentsUsuario = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);

        //listaGrupos.setAdapter(adaptadorGrupos);
        //esdevenimentsUsuario.setAdapter(adaptadorEsdeveniments);

    }

    public void mostrarInfoUsuario(UsuarioEntity usuario) {
        nombre.setText(usuario.getNickname());
        //System.out.println(usuario.getNickname());
        foto.setImageResource(R.drawable.redhot); //Cambiar con imagen de usuario
        Map<String,Boolean> idGrupos = usuario.getIdGrupos();
        Map<String,Boolean> idEventos = usuario.getIdEventos();
        //for (Map.Entry<String, Boolean> entry : idGrupos.entrySet()){
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            //if(entry.getValue()) {
                idGrupo = "-KUbHqRIqgL1eDGWpHT0";
                gMGR.getInfoGrupoUsuario(this,/*entry.getKey()*/idGrupo);
            //}
        //}
        //for (Map.Entry<String, Boolean> entry : idEventos.entrySet()){
                //System.out.println(entry.getKey() + "/" + entry.getValue());
                //if(entry.getValue()) {
                    idEvento = "-KUavWyMfmX-uxtRqMo5";
                    eMGR.getInfoEventoUsuario(this,/*entry.getKey()*/idEvento);
                //}
        //}
    }

    public void rellenarListaGrupos (GrupoEntity grupo){
        //System.out.println(grupo.getNickname()+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(grupo.getImagen());
        String img = grupo.getImagen();
        Bitmap imBM = StringToBitMap(img);
        String nombreGrupo = grupo.getNombreGrupo();
        Info info = new Info(imBM, nombreGrupo, "adeu");
        grupos.add(info);
        //System.out.println(grupos.get(0).primeraLinea);
        ListAdapter adaptadorGrupos = new CustomAdapterGruposActivity(this, grupos);
        listaGrupos.setAdapter(adaptadorGrupos);
        //hideProgressDialog();
    }

    public void rellenarListaEventos (EventoEntity evento) {
        //System.out.println(evento.getTitulo()+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(evento.getHorario());
        String img = evento.getImagen();
        Bitmap imBM = StringToBitMap(img);
        String nombreGrupo = evento.getTitulo();
        String horario = evento.getHorario();
        Info info = new Info(imBM, nombreGrupo, horario);
        grupos.add(info);
        //System.out.println(grupos.get(0).primeraLinea);
        //System.out.println(grupos.get(0).segonaLinea);
        ListAdapter adaptadorEsdeveniments = new CustomAdapterEsdevenimentsActivity(this, eventos);
        listaEventos.setAdapter(adaptadorEsdeveniments);
        hideProgressDialog();
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