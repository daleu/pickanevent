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

public class CrearEventoActivity extends BaseActivity {


    public static final int GALERIA_REQUEST = 20;
    EventoMGR eMGR;
    Bitmap image;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(activity_crear_evento);
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFecha);
                data.setText(day + " de " + ViewUtils.getNomMes(month) + " de " + year);
            }
        });
        final CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        calendarFinal.setVisibility(View.INVISIBLE);
        calendarFinal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFechaFinal);
                data.setText(day + " de " + ViewUtils.getNomMes(month) + " de " + year);
            }
        });
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        EditText hora = (EditText) findViewById(R.id.hora);
        preuText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        hora.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        EditText data = (EditText) findViewById(R.id.editorFecha);
        EditText dataFinal = (EditText) findViewById(R.id.editorFechaFinal);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendar(view);
            }
        });
        dataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendarFinal(view);
            }
        });

    }

    public void crearEvento(View _view) {
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
        EditText dataFinal = (EditText) findViewById(R.id.editorFechaFinal);
        EditText hora = (EditText) findViewById(R.id.hora);



        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 75, bYtE);
        image.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imatge = Base64.encodeToString(byteArray, Base64.DEFAULT);

        EventoEntity ee;
        if (dataFinal.getText().toString().matches("")) {
             ee = new EventoEntity(nomEvent.getText().toString(), descripcio.getText().toString(), imatge, preu,
                    url.getText().toString(), localitzacio.getText().toString(),
                     "El " + data.getText().toString() + " a las " + hora.getText().toString(), "", "");
        } else {
            String interval = "Del " + data.getText().toString() + " al " + dataFinal.getText().toString() + " a las " + hora.getText().toString();
            ee = new EventoEntity(nomEvent.getText().toString(), descripcio.getText().toString(), imatge, preu,
                    url.getText().toString(), localitzacio.getText().toString(), interval, "", "");
        }

        //eMGR = new EventoMGR().getInstance(); VIEJA
        eMGR = MGRFactory.getInstance().getEventoMGR(); //NUEVA
        eMGR.crear(ee);
        Toast.makeText(this,"Evento creado",Toast.LENGTH_LONG).show();
    }

    public void comprovarCheckBox(View _view) {
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

    public void mostrarCalendar(View _view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        if (calendar.getVisibility() == _view.VISIBLE) {
            calendar.setVisibility(_view.INVISIBLE);
        }
        else {
            calendar.setVisibility(_view.VISIBLE);
            calendarFinal.setVisibility(_view.INVISIBLE);
        }
    }

    public void mostrarCalendarFinal(View _view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarViewFinal);
        CalendarView calendarInicial = (CalendarView) findViewById(R.id.calendarView);
        if (calendar.getVisibility() == _view.VISIBLE) {
            calendar.setVisibility(_view.INVISIBLE);
        }
        else {
            calendar.setVisibility(_view.VISIBLE);
            calendarInicial.setVisibility(_view.INVISIBLE);
        }
    }

    public void abrirGaleria (View _view) {
        Intent galeria = new Intent(Intent.ACTION_PICK);
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String dirGaleria = directorio.getPath();
        Uri data = Uri.parse(dirGaleria);

        galeria.setDataAndType(data,"image/*"); //get all image types

        startActivityForResult(galeria, GALERIA_REQUEST); //image return
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        if (_resultCode == RESULT_OK) {
            if (_requestCode == GALERIA_REQUEST) {
                Uri imageUri = _data.getData();
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
}
