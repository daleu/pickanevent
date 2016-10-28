package com.pes12.pickanevent.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import static com.pes12.pickanevent.R.layout.activity_crear_evento;

public class CrearEventoActivity extends BaseActivity{


    public static final int GALERIA_REQUEST = 20;
    EventoMGR eMGR;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_crear_evento);
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFecha);
                data.setText(day + " de " + getNomMes(month) + " de " + year);
            }
        });
        final CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        calendarFinal.setVisibility(View.INVISIBLE);
        calendarFinal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFechaFinal);
                data.setText(day + " de " + getNomMes(month) + " de " + year);
            }
        });
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        preuText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void crearEvento(View view) {
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



        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 75, bYtE);
        image.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imatge = Base64.encodeToString(byteArray, Base64.DEFAULT);

        EventoEntity ee = new EventoEntity(nomEvent.getText().toString(),descripcio.getText().toString(),imatge,preu,
                url.getText().toString(),localitzacio.getText().toString(),data.getText().toString(),"","");

        //eMGR = new EventoMGR().getInstance(); VIEJA
        eMGR = MGRFactory.getInstance().getEventoMGR(); //NUEVA
        eMGR.crear(ee);
        Toast.makeText(this,"Evento creado",Toast.LENGTH_LONG).show();
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
        CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        if (calendar.getVisibility() == view.VISIBLE) {
            calendar.setVisibility(view.INVISIBLE);
        }
        else {
            calendar.setVisibility(view.VISIBLE);
            calendarFinal.setVisibility(view.INVISIBLE);
        }
    }

    public void mostrarCalendarFinal(View view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarViewFinal);
        CalendarView calendarInicial = (CalendarView) findViewById(R.id.calendarView);
        if (calendar.getVisibility() == view.VISIBLE) {
            calendar.setVisibility(view.INVISIBLE);
        }
        else {
            calendar.setVisibility(view.VISIBLE);
            calendarInicial.setVisibility(view.INVISIBLE);
        }
    }

    public void abrirGaleria (View view) {
        Intent galeria = new Intent(Intent.ACTION_PICK);
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String dirGaleria = directorio.getPath();
        Uri data = Uri.parse(dirGaleria);

        galeria.setDataAndType(data,"image/*"); //get all image types

        startActivityForResult(galeria, GALERIA_REQUEST); //image return
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALERIA_REQUEST) {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    ImageView imgV = (ImageView) findViewById(R.id.imagenEvento);
                    image = BitmapFactory.decodeStream(inputStream);
                    //show the image to the user
                    imgV.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "No s'ha pogut obrir la imatge", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private String getNomMes(int month) {
        String nom;
        if (month == 1) nom = "Enero";
        else if (month == 2) nom = "Febrero";
        else if (month == 3) nom = "Marzo";
        else if (month == 4) nom = "Abril";
        else if (month == 5) nom = "Mayo";
        else if (month == 6) nom = "Junio";
        else if (month == 7) nom = "Julio";
        else if (month == 8) nom = "Agosto";
        else if (month == 9) nom = "Septiembre";
        else if (month == 10) nom = "Octubre";
        else if (month == 11) nom = "Noviembre";
        else nom = "Diciembre";
        return nom;
    }
}
