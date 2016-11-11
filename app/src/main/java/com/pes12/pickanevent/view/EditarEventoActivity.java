package com.pes12.pickanevent.view;

import com.pes12.pickanevent.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

import static com.pes12.pickanevent.view.CrearEventoActivity.GALERIA_REQUEST;

public class EditarEventoActivity extends BaseActivity {
    EventoMGR eMGR;
    String idEvento = "-KV9U5W-oL1c7xF9fXt4";
    Bitmap image;

    private ImageView imagenEvento;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        eMGR.getInfoEventoEditar(this);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFecha);
                data.setText(day + " de " + ViewUtils.getNomMes(month) + " de " + year);

            }
        });
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        preuText.setRawInputType(InputType.TYPE_CLASS_NUMBER);

    }

    public void mostrarInfoEvento (Map<String,EventoEntity> _ge) {
        EventoEntity evento = _ge.get(idEvento);

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
        image = StringToBitMap(img);
        imagenEvento.setImageBitmap(image);
        imagenEvento.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private Bitmap StringToBitMap(String _encodedString) {
        try {
            byte[] encodeByte = Base64.decode(_encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void guardarEvento (View _view) {
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
        eMGR.actualizar(idEvento,ee);
        Toast.makeText(this,"Evento guardado",Toast.LENGTH_LONG).show();
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
        if (calendar.getVisibility() == _view.VISIBLE) {
            calendar.setVisibility(_view.INVISIBLE);
        }
        else {
            calendar.setVisibility(_view.VISIBLE);
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
