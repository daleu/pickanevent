package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class VerInfoEvento extends AppCompatActivity {

    private TextView descripcion;
    private TextView titulo;
    private TextView horarios;
    private TextView precio;
    private TextView lugar;

    private ImageView imagenevento;

    private Button comprarEntradas;

    private String idEvento;
    private String web;

    EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_info_evento);

        //Poner iconos
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TextView clockIcon = (TextView) findViewById(R.id.clockIcon);
        clockIcon.setTypeface(fontAwesomeFont);
        TextView dollarIcon = (TextView) findViewById(R.id.clockDollar);
        dollarIcon.setTypeface(fontAwesomeFont);
        TextView pinIcon = (TextView) findViewById(R.id.mapIcon);
        pinIcon.setTypeface(fontAwesomeFont);

        //Consultar informacion
        eMGR = new EventoMGR().getInstance();
        eMGR.getInfoGrupo(this);

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

    public void mostrarInfoEvento(Map<String,EventoEntity> ge) {
        idEvento = "-KUaTbYM2S8Y92OuT_ow";
        EventoEntity gEntity = ge.get(idEvento);

        imagenevento = (ImageView)findViewById(R.id.imagenEvento);
        comprarEntradas = (Button) findViewById(R.id.buttonPreus);
        descripcion = (TextView) findViewById(R.id.descripcion);
        titulo = (TextView) findViewById(R.id.textEvento);
        horarios = (TextView) findViewById(R.id.textHora);
        precio = (TextView) findViewById(R.id.textPreu);
        lugar = (TextView) findViewById(R.id.textMap);

        descripcion.setText(gEntity.getDescripcion());
        titulo.setText(gEntity.getTitulo());
        horarios.setText(gEntity.getHorario());
        precio.setText(gEntity.getPrecio());
        lugar.setText(gEntity.getLocalizacion());

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
