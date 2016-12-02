package com.pes12.pickanevent.view;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.pes12.pickanevent.R.layout.activity_crear_evento;

public class CrearEventoActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {


    public static final int GALERIA_REQUEST = 20;
    private static final LatLngBounds bounds =
            new LatLngBounds(new LatLng(35.871045, -9.919695), new LatLng(42.957396, 4.729860));
    //------------------- GOOGLE PLACES API ------------------
    protected GoogleApiClient mGoogleApiClient;
    EventoMGR eMGR;
    Bitmap image;
    String lat;
    String lng;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            final LatLng latlng = place.getLatLng();
            String coordenades[] = latlng.toString().split(",");
            lat = coordenades[0].substring(10);
            lng = coordenades[1].replace(")", "");


            //Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            //Toast.makeText(activity_crear_evento, "Autocomplete item selected: " + primaryText, Toast.LENGTH_SHORT);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            //Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        //Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
        //websiteUri));
        //return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
        //  websiteUri));

        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);

        //-------------- GOOGLE PLACES API -------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Places.GEO_DATA_API)
                .build();
        //-----------------------------------------------


        setContentView(activity_crear_evento);
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFecha);
                data.setText(day + " de " + ViewSharedMethods.getNomMes(month, getApplicationContext()) + " de " + year);
            }
        });
        final CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        calendarFinal.setVisibility(View.INVISIBLE);
        calendarFinal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                EditText data = (EditText) findViewById(R.id.editorFechaFinal);
                data.setText(day + " de " + ViewSharedMethods.getNomMes(month, getApplicationContext()) + " de " + year);
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


        //--------------------- GOOGLE PLACES API -----------------
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.editorLugar);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, bounds,
                null);
        mAutocompleteView.setAdapter(mAdapter);
    }

    public void crearEvento(View _view) {
        EventoEntity ee = parseEventViewToEntity(image, lat, lng);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        eMGR.crear(ee);
        Toast.makeText(this, R.string.DEFAULT_EVENTO_CREADO, Toast.LENGTH_LONG).show();
    }

    public void comprovarCheckBox(View _view) {
        CheckBox gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        EditText preuText = (EditText) findViewById(R.id.editorPrecio);
        if (gratuit.isChecked()) {
            preuText.setFocusable(false);
            preuText.setText("");
            preuText.setHint(R.string.ESCRIBE_PRECIO_EVENTO);
        } else {
            preuText.setFocusableInTouchMode(true);
            preuText.setFocusable(true);
        }
    }

    public void mostrarCalendar(View _view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        CalendarView calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        if (calendar.getVisibility() == _view.VISIBLE) {
            calendar.setVisibility(_view.INVISIBLE);
        } else {
            calendar.setVisibility(_view.VISIBLE);
            calendarFinal.setVisibility(_view.INVISIBLE);
        }
    }


    //---------------------- GOOGLE PLACES API ---------------

    public void mostrarCalendarFinal(View _view) {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarViewFinal);
        CalendarView calendarInicial = (CalendarView) findViewById(R.id.calendarView);
        if (calendar.getVisibility() == _view.VISIBLE) {
            calendar.setVisibility(_view.INVISIBLE);
        } else {
            calendar.setVisibility(_view.VISIBLE);
            calendarInicial.setVisibility(_view.INVISIBLE);
        }
    }

    public void abrirGaleria(View _view) {
        Intent galeria = new Intent(Intent.ACTION_PICK);
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String dirGaleria = directorio.getPath();
        Uri data = Uri.parse(dirGaleria);

        galeria.setDataAndType(data, "image/*"); //get all image types

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
