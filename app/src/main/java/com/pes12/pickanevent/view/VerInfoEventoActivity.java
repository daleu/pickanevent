package com.pes12.pickanevent.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.pes12.pickanevent.R.id.borrarCuenta;
import static com.pes12.pickanevent.R.id.nombreGrupo;

public class VerInfoEventoActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TWITTER_KEY = "PUlLyuMrqQzt61r7dmHgy6b6W";
    private static final String TWITTER_SECRET = "EoOyglsIzCZZJ4ghHBU2ZoLgUduoPEGYuSy1mZZmrI7IjlVigQ";

    private TextView descripcion;
    private TextView titulo;
    private TextView horarios;
    private TextView precio;
    private TextView lugar;

    private ImageView imagenevento;

    private Button comprarEntradas;
    private Button share;


    private String idEvento;
    private String web;
    private String latitud;
    private String longitud;

    private MapFragment mapFragment;
    private Button borrarEvento;
    private RelativeLayout layoutEdit;
    Bundle param;

    private EventoMGR eMGR;
    private GrupoMGR gMGR;
    GrupoEntity grupo;
    private String idGrupo;
    private String nombreGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_evento);
        showProgressDialog();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig),new TweetComposer());

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        param = getIntent().getExtras();
        //idEvento = "-K_xhR3NMID-9FN6W4Ym";
        if(param.getString("key")!=null){
            idEvento = param.getString("key");
        }
        else {
            Toast.makeText(this, "Ya existe un evento con este nombre", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerInfoEventoActivity.this, CrearEventoActivity.class));
        }

        //Poner iconos
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TextView clockIcon = (TextView) findViewById(R.id.clockIcon);
        clockIcon.setTypeface(fontAwesomeFont);
        TextView dollarIcon = (TextView) findViewById(R.id.clockDollar);
        dollarIcon.setTypeface(fontAwesomeFont);
        TextView pinIcon = (TextView) findViewById(R.id.mapIcon);
        pinIcon.setTypeface(fontAwesomeFont);

        //Consultar informacion
        eMGR = MGRFactory.getInstance().getEventoMGR(); //NUEVA
        eMGR.getInfoEvento(this,idEvento);

        //Boton editar evento
        layoutEdit = (RelativeLayout) findViewById(R.id.layoutEdit);
        if (getUsuarioActual().getCm())
            layoutEdit.setVisibility(View.VISIBLE);
        else
            layoutEdit.setVisibility(View.INVISIBLE);

        //Boton eliminar grupo
        borrarEvento = (Button) findViewById(R.id.borrarEvento);
        if (getUsuarioActual().getCm()) {
            borrarEvento.setOnClickListener(new View.OnClickListener() {
                Boolean esCM = getUsuarioActual().getCm();

                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = null;
                    dialoglayout = inflater.inflate(R.layout.dialog_borrar_grupo, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerInfoEventoActivity.this);
                    builder.setView(dialoglayout);
                    final AlertDialog alert = builder.create();
                    alert.show();
                    Button aceptar = (Button) dialoglayout.findViewById(borrarCuenta);
                    Button cancelar = (Button) dialoglayout.findViewById(R.id.funcionVacia);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Boolean noError = borrarEvento(idEvento, false);
                            String msg = noError ? getString(R.string.BORRADO_EVENTO_CORRECTO) : getString(R.string.ERROR_BORRAR);
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            alert.hide();
                            if (noError) signOut();
                        }
                    });
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.hide();
                        }
                    });
                }
            });
        }else{
            borrarEvento.setVisibility(View.INVISIBLE);
        }
    }

    public void post(View _view) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Te recomiendo el evento "
                        + titulo.getText().toString() + " de " + nombreGrupo
                        + "!\nMe acompañas? \n\n"
                        + "#PickAnEvent");
        builder.show();
    }

    public void mostrarInfoGrupo (GrupoEntity _ge) {
        grupo = _ge;
        nombreGrupo = _ge.getNombreGrupo();
    }

    public void mostrarInfoEvento(Map<String, EventoEntity> ge) {

        EventoEntity gEntity = ge.get(idEvento);

        if(param.getString("action")!=null){
            Log.e("action",param.getString("action"));
            if(param.getString("action").equals("assistir")){
                asistirEvento(idEvento,gEntity.getTitulo());
                Log.e("action",param.getString("action"));
            }
            else if(param.getString("action").equals("noassistir")){
                cancelarAsistenciaEvento(idEvento);
                Log.e("action",param.getString("action"));
            }
        }

        imagenevento = (ImageView) findViewById(R.id.imagenEvento);
        comprarEntradas = (Button) findViewById(R.id.buttonPreus);
        descripcion = (TextView) findViewById(R.id.descripcion);
        titulo = (TextView) findViewById(R.id.textEvento);
        horarios = (TextView) findViewById(R.id.textHora);
        precio = (TextView) findViewById(R.id.textPreu);
        lugar = (TextView) findViewById(R.id.textMapa);

        if(gEntity.getDescripcion()!=null)descripcion.setText(gEntity.getDescripcion());
        titulo.setText(gEntity.getTitulo());

        idGrupo = gEntity.getIdGrup();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        gMGR.getInfoGrupoEvento(VerInfoEventoActivity.this,idGrupo);

        Date dataI = gEntity.getDataInDate();
        Date dataF = gEntity.getDataFiDate();

        String hora1;
        String hora2;

        if (dataI.getHours() < 10) {
            if (dataI.getMinutes() < 10) hora1 = "0" + dataI.getHours() + ":0" + dataI.getMinutes();
            else hora1 = "0" + dataI.getHours() + ":" + dataI.getMinutes();
        }
        else {
            if (dataI.getMinutes() < 10) hora1 = dataI.getHours() + ":0" + dataI.getMinutes();
            else hora1 = dataI.getHours() + ":" + dataI.getMinutes();
        }

        if (dataI.getHours() < 10) {
            if (dataI.getMinutes() < 10) hora2 = "0" + dataI.getHours() + ":0" + dataI.getMinutes();
            else hora2 = "0" + dataI.getHours() + ":" + dataI.getMinutes();
        }
        else {
            if (dataI.getMinutes() < 10) hora2 = dataI.getHours() + ":0" + dataI.getMinutes();
            else hora2 = dataI.getHours() + ":" + dataI.getMinutes();
        }

        String dIni = dataI.getDay()+1 + "/" + dataI.getMonth()+1 + "/" + (dataI.getYear()+1900) + " a las " + hora1;
        String dFi = dataF.getDay()+1 + "/" + dataF.getMonth()+1 + "/" + (dataF.getYear()+1900) + " a las " + hora2;

        horarios.setText(dIni + " hasta el\n" +dFi);

        if(gEntity.getPrecio().equals("")){
            precio.setText("Gratis");
        }
        else{
            precio.setText(gEntity.getPrecio());
        }
        lugar.setText(gEntity.getLocalizacion());

        latitud = gEntity.getLatitud();
        longitud = gEntity.getLongitud();

        web = gEntity.getWebpage();

        comprarEntradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(web));
                startActivity(i);
            }
        });

        Picasso.with(this).load(gEntity.getImagen()).into(imagenevento);

        //centrar mapa y poner pinlocation
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)))
                .title(lugar.getText().toString()));
    }

    public void goEditarEvento(View _view) {
        startActivity(new Intent(VerInfoEventoActivity.this, EditarEventoActivity.class).putExtra("idEvento",idEvento));
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerInfoEventoActivity.this, NavigationDrawer.class));
    }
}
