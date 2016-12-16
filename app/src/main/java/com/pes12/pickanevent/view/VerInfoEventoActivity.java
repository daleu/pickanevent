package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Map;

public class VerInfoEventoActivity extends BaseActivity implements OnMapReadyCallback {

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

    private EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_evento);
        showProgressDialog();

        Bundle param = getIntent().getExtras();
        idEvento = "Evt102-1480690194876";
        if(param!=null){
            idEvento = param.getString("key");
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
        //eMGR = new EventoMGR().getInstance(); VIEJA
        eMGR = MGRFactory.getInstance().getEventoMGR(); //NUEVA
        eMGR.getInfoEvento(this);

        //Crear Evento
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.redhot);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        EventoEntity ge = new EventoEntity("Red Hot Chilli Peppers a Barcelona",
                "Anthony Kiedis (voz), Flea (bajo), Chad Smith (batería) y Josh Klinghoffer (guitarra) regresan a nuestro país cinco años después de sus últimos conciertos. Red Hot Chili Peppers pisarán de nuevo el escenario del Palau Sant Jordi en el que presentarán su nuevo trabajo “The Getaway”.",
                imageFile,
                "90",
                "http://www.ticketmaster.es",
                "Palau Sant Jordi",
                "dissabte, 1 / octubre de 21:00 a 0:00 \n 1 octubre (21:00) - 2 octubre (0:00)");
        eMGR.crear(ge);*/
    }

    public void post(View _view) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Te recomiendo el evento "
                        + titulo.getText().toString() +" en " + lugar.getText().toString()
                        + "!\nMe acompañas? \n\n"
                        + "PickAnEvent");
        builder.show();
    }

    public void mostrarInfoEvento(Map<String, EventoEntity> ge) {

        EventoEntity gEntity = ge.get(idEvento);

        imagenevento = (ImageView) findViewById(R.id.imagenEvento);
        comprarEntradas = (Button) findViewById(R.id.buttonPreus);
        descripcion = (TextView) findViewById(R.id.descripcion);
        titulo = (TextView) findViewById(R.id.textEvento);
        horarios = (TextView) findViewById(R.id.textHora);
        precio = (TextView) findViewById(R.id.textPreu);
        lugar = (TextView) findViewById(R.id.textMapa);

        descripcion.setText(gEntity.getDescripcion());
        titulo.setText(gEntity.getTitulo());
        horarios.setText("horario");
        precio.setText(gEntity.getPrecio());
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

        Bitmap imgBM = StringToBitMap(gEntity.getImagen());
        imagenevento.setImageBitmap(imgBM);
        imagenevento.setScaleType(ImageView.ScaleType.FIT_XY);

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

    public void asistirEvento() {
        if (!asistiendoEvento(getUsuarioActual(),idEvento)){
            asistirEvento(getUsuarioActual().getId(),getUsuarioActual(),idEvento,titulo.getText().toString());
        }
        else
            Toast.makeText(this,"Ya asistes a este evento",Toast.LENGTH_SHORT).show();
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
}
