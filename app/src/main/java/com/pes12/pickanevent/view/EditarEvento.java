package com.pes12.pickanevent.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterListaEventos;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class EditarEvento extends AppCompatActivity {

    EventoMGR eMGR;

    private TextView descripcion;
    private TextView titulo;
    private TextView horarios;
    private TextView precio;
    private TextView lugar;

    private ImageView imagenevento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        eMGR = new EventoMGR().getInstance();
        eMGR.getInfoEvento(this);
    }

    public void mostrarInfoEvento (Map<String,EventoEntity> ge) {
        String idEvento = "-KUb2U2qvQg2ODYApjxs";
        EventoEntity evento = ge.get(idEvento);


        EditText nomEvent = (EditText) findViewById(R.id.editorNEvento);
        EditText descripcio = (EditText) findViewById(R.id.editorDescr);
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        EditText url = (EditText) findViewById(R.id.editorEntradas);
        EditText localitzacio = (EditText) findViewById(R.id.editorLugar);
        EditText data = (EditText) findViewById(R.id.editorFecha);


        nomEvent.setText(evento.getTitulo());
        descripcio.setText(evento.getDescripcion());
        String img = evento.getImagen();
        data.setText(evento.getHorario());
        if (evento.getPrecio().equals("-1")) {
            gratuit.setChecked(true);
            preuText.setFocusable(false);
            preuText.setText("");
            preuText.setHint("Escriba el precio del evento");
        }
        else {
            gratuit.setChecked(false);
            preuText.setText(evento.getPrecio());
        }
        localitzacio.setText(evento.getLocalizacion());
        url.setText(evento.getWebpage());

        Bitmap imgBM = StringToBitMap(img);
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

    public void guardarEvento(View view) {
        EditText nomEvent = (EditText) findViewById(R.id.editorNEvento);
        EditText descripcio = (EditText) findViewById(R.id.editorDescr);
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        String preu;
        if (gratuit.isChecked()) preu = "-1";
        else {
            EditText preuText = (EditText) findViewById(R.id.editorPrecio);
            preu = preuText.getText().toString();
        }
        EditText url = (EditText) findViewById(R.id.editorEntradas);
        EditText localitzacio = (EditText) findViewById(R.id.editorLugar);
        EditText data = (EditText) findViewById(R.id.editorFecha);



        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.oso);
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bm.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imatge = Base64.encodeToString(byteArray, Base64.DEFAULT);

        EventoEntity ee = new EventoEntity(nomEvent.getText().toString(),descripcio.getText().toString(),imatge,preu,
                url.getText().toString(),localitzacio.getText().toString(),data.getText().toString());

        eMGR = new EventoMGR().getInstance();
        eMGR.actualizar("-KUb2U2qvQg2ODYApjxs", ee);
        Toast.makeText(this,"Cambios guardados",Toast.LENGTH_LONG).show();
    }
}
