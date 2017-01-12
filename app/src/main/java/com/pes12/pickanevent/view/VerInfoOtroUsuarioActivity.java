package com.pes12.pickanevent.view;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class VerInfoOtroUsuarioActivity extends BaseActivity {

    private TextView nombre;
    private ImageView foto;
    private ListView listaGrupos;
    private ListView listaEventos;
    private Button boton;


    private String idUsuario;
    private String idGrupo;
    private String idEvento;

    private ArrayList<Info> grupos;
    private ArrayList<Info> eventos;

    private UsuarioMGR uMGR;
    private GrupoMGR gMGR;
    private EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_otro_usuario);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        //Progres dialog
        showProgressDialog();

        //Inicializaciones
        nombre = (TextView) findViewById(R.id.textNombreUsuario);
        foto = (ImageView) findViewById(R.id.imagenOtroUsuario);
        listaGrupos = (ListView) findViewById(R.id.listaGruposUsuario);
        listaEventos = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);
        grupos = new ArrayList<Info>();
        eventos = new ArrayList<Info>();
        ///////////////////////////////////////////////////////////////////////

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        eMGR = MGRFactory.getInstance().getEventoMGR();


        Bundle param = getIntent().getExtras();
        idUsuario = "usr15-1483884870397";
        if(param!=null){
            idUsuario = param.getString("key");

            if(param.getString("action")!=null){
                if(param.getString("action").equals("noseguir")) dejarSeguirUsuario(idUsuario);
            }
        }

        uMGR.getInfoUsuario(this, idUsuario);

        boton = (Button) findViewById(R.id.seguir);
        String texto;
        if (siguiendoUsuario(idUsuario)) texto = getString(R.string.DEFAULT_NO_SEGUIR);
        else texto = getString(R.string.DEFAULT_SEGUIR);
        boton.setText(texto);



        //ListView gruposUsuario = (ListView) findViewById(R.id.listaGruposUsuario);
        //ListView esdevenimentsUsuario = (ListView) findViewById(R.id.listaEsdevenimentsUsuario);

        //listaGrupos.setAdapter(adaptadorGrupos);
        //esdevenimentsUsuario.setAdapter(adaptadorEsdeveniments);

    }

    public void mostrarInfoUsuario(UsuarioEntity usuario) {
        nombre.setText(usuario.getNickname());
        Log.e("nickname",usuario.getNickname());
        if  (usuario != null) {
            if (usuario.getUrlPhoto() != null) {
                Picasso.with(this).load(usuario.parsedPhotoURI()).into(foto);
            }
            ; //Cambiar con imagen de usuario
        }
        Map<String, String> idGrupos = usuario.getIdGrupos();
        Map<String, String> idEventos = usuario.getIdEventos();

        if(idGrupos!=null) {
            for (Map.Entry<String, String> entry : idGrupos.entrySet()) {
                //if(entry.getValue()) {
                //idGrupo = "-KUbHqRIqgL1eDGWpHT0";
                gMGR.getInfoGrupoUsuario(this, entry.getKey());
                //}
            }
        }
        if(idEventos!=null) {
            for (Map.Entry<String, String> entry : idEventos.entrySet()) {
                //System.out.println(entry.getKey() + "/" + entry.getValue());
                //if(entry.getValue()) {
                //idEvento = "-KUavWyMfmX-uxtRqMo5";
                eMGR.getInfoEventoUsuario(this, entry.getKey());
                //}
            }
        }
        if (idEventos==null && idGrupos==null) hideProgressDialog();
    }

    public void rellenarListaGrupos(GrupoEntity grupo, String id) {
        //System.out.println(grupo.getNickname()+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(grupo.getImagen());
        if(grupo!=null) {
            String img = grupo.getImagen();
            String nombreGrupo = grupo.getNombreGrupo();
            Info info = new Info(img, nombreGrupo, null, getString(R.string.DEFAULT_SEGUIR));
            info.setId(id);
            info.setTipus(Constantes.INFO_GRUPO);
            info.setBotonVisible(false);
            eventos.add(info);
            //System.out.println(grupos.get(0).primeraLinea);
            AdapterLista ale = new AdapterLista(VerInfoOtroUsuarioActivity.this, R.layout.vista_adapter_lista, eventos);
            listaGrupos.setAdapter(ale);
        }
        hideProgressDialog();
    }

    public void rellenarListaEventos(EventoEntity evento, String id) {
        //System.out.println(evento.getTitulo()+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(evento.getHorario());
        if(evento!=null) {
            String img = evento.getImagen();
            //Bitmap imBM = StringToBitMap(img);
            String nombreGrupo = evento.getTitulo();
            String dataInici=null;
            String dataFi=null;
            Date dataI = evento.getDataInDate();
            Date dataF = evento.getDataFiDate();
            Calendar c = Calendar.getInstance();
            c.setTime(dataI);
            dataInici =(c.get(Calendar.DAY_OF_MONTH) + " de " + ViewSharedMethods.getNomMes(
                    dataI.getMonth()+1, getApplicationContext()) + " de " + (dataI.getYear()+1900)+ " "+ dataI.getHours() + ":" + dataI.getMinutes());
            c.setTime(dataF);
            dataFi = (c.get(Calendar.DAY_OF_MONTH) + " de " + ViewSharedMethods.getNomMes(
                    dataF.getMonth()+1, getApplicationContext()) + " de " + (dataI.getYear()+1900)+ " "+ dataF.getHours() + ":" + dataF.getMinutes());


            Info info = new Info(img, nombreGrupo, dataInici +"\r"+ dataFi, getString(R.string.DEFAULT_SEGUIR));
            info.setId(id);
            info.setTipus(Constantes.INFO_EVENTO);
            info.setBotonVisible(false);
            grupos.add(info);
            //System.out.println(grupos.get(0).primeraLinea);
            //System.out.println(grupos.get(0).segonaLinea);
            AdapterLista alg = new AdapterLista(VerInfoOtroUsuarioActivity.this, R.layout.vista_adapter_lista, grupos);
            listaEventos.setAdapter(alg);
        }
        hideProgressDialog();
    }

    public void seguirNoSeguir(View view) {
        UsuarioEntity currentUser = getUsuarioActual();
        String texto;
        if (!currentUser.getCm()) {
            if (siguiendoUsuario(idUsuario)) { //el usuario quiere no seguir a este usuario
                dejarSeguirUsuario(idUsuario);
                texto = getString(R.string.DEFAULT_SEGUIR);
            }
            else {
                seguirUsuario(idUsuario, nombre.getText().toString());
                texto = getString(R.string.DEFAULT_NO_SEGUIR);
            }
            boton.setText(texto);
        }
    }
}