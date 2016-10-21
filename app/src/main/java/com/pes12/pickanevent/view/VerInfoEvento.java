package com.pes12.pickanevent.view;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

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

    EventoMGR gMGR;

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

    }

    public void mostrarInfoEvento(Map<String,EventoEntity> ge) {
        idEvento = "-KUS-QRDjJ3SuTFktAZd";
        EventoEntity gEntity = ge.get(idEvento);
        descripcion.setText(gEntity.getDescripcion());
        imagenevento = (ImageView)findViewById(R.id.imagenGrupo);

        /* COMPRESION IMAGEN PARA GUARDARLA EN FIREBASE COMO STRING
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.oso);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        */

        Drawable myDrawable = getResources().getDrawable(R.drawable.oso);
        imagenevento.setImageDrawable(myDrawable);
    }
}
