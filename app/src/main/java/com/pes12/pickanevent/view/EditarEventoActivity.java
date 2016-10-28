package com.pes12.pickanevent.view;

import com.pes12.pickanevent.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class EditarEventoActivity extends BaseActivity {
    EventoMGR eMGR;
    String idEvento = "-KUvpkweDzzNk0P3bqhY";

    private ImageView imagenEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        eMGR.getInfoEventoEditar(this);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFecha);
                if (day < 10 || month < 10) {
                    if (day < 10 && month < 10) data.setText("0" + day + "/0" + month + "/" + year);
                    else if (day < 10) data.setText("0" + day + "/" + month + "/" + year);
                    else if (month < 10) data.setText(day + "/0" + month + "/" + year);
                }
                else data.setText(day + "/" + month + "/" + year);
            }
        });
    }

    public void mostrarInfoEvento (Map<String,EventoEntity> ge) {
        EventoEntity evento = ge.get(idEvento);

        EditText nomEvent = (EditText) findViewById(R.id.editorNEvento);
        EditText descripcio = (EditText) findViewById(R.id.editorDescr);
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        EditText url = (EditText) findViewById(R.id.editorEntradas);
        EditText localitzacio = (EditText) findViewById(R.id.editorLugar);
        EditText data = (EditText) findViewById(R.id.editorFecha);
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);

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


        imagenEvento = (ImageView) findViewById(R.id.imagenEvento);
        Bitmap imgBM = StringToBitMap(img);
        imagenEvento.setImageBitmap(imgBM);
        imagenEvento.setScaleType(ImageView.ScaleType.FIT_XY);
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

    public void guardarEvento (View view) {
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
                url.getText().toString(),localitzacio.getText().toString(),data.getText().toString(),"","");

        //eMGR = new EventoMGR().getInstance(); VIEJA
        eMGR = MGRFactory.getInstance().getEventoMGR(); //NUEVA
        eMGR.actualizar(idEvento,ee);
        Toast.makeText(this,"Evento guardado",Toast.LENGTH_LONG).show();
    }

    public void comprovarCheckBox(View view) {
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        if (gratuit.isChecked()) {
            preuText.setFocusable(false);
            preuText.setText("");
            preuText.setHint("Escriba el precio del evento");
        }
        else {
            preuText.setFocusableInTouchMode(true);
            preuText.setFocusable(true);
        }
    }

    public void mostrarCalendar(View view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        if (calendar.getVisibility() == view.VISIBLE) {
            calendar.setVisibility(view.INVISIBLE);
        }
        else {
            calendar.setVisibility(view.VISIBLE);
        }
    }
}
