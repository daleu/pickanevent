package com.pes12.pickanevent.view;


import android.content.ContentResolver;
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
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.ImagenEvento.ImagenEventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.PlaceAutocompleteAdapter;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.pes12.pickanevent.R.layout.activity_crear_evento;

public class CrearEventoActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText preuText;
    EditText hora;
    EditText horaFi;
    EditText data;
    EditText dataFinal;
    CheckBox gratuit;
    CalendarView calendar;
    CalendarView calendarFinal;
    InputStream isImagen;

    EditText nomEvent;
    EditText descripcio;
    EditText url;
    EditText localitzacio;
    ImageView imgV;


    ImagenEventoMGR iMGR;

    String idGrupo;
    GrupoEntity grupo;

    private Date dataIni;
    private Date dataFi;
    public static final int GALERIA_REQUEST = 20;
    private static final LatLngBounds bounds =
            new LatLngBounds(new LatLng(35.871045, -9.919695), new LatLng(42.957396, 4.729860));
    //------------------- GOOGLE PLACES API ------------------
    protected GoogleApiClient mGoogleApiClient;
    EventoMGR eMGR;
    GrupoMGR gMGR;
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

            Toast.makeText(getApplicationContext(), getString(R.string.HAS_SELECCIONAT) + primaryText,
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

        idGrupo = getIntent().getExtras().getString("key");
        grupo = (GrupoEntity) getIntent().getExtras().getSerializable("grupo");;

        //-------------- GOOGLE PLACES API -------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Places.GEO_DATA_API)
                .build();
        //-----------------------------------------------

        nomEvent = (EditText) findViewById(R.id.editorNEvento);
        descripcio = (EditText) findViewById(R.id.editorDescr);
        url = (EditText) findViewById(R.id.editorEntradas);
        localitzacio = (EditText) findViewById(R.id.editorLugar);

        iMGR = MGRFactory.getInstance().getImagenEventoMGR();

        setContentView(activity_crear_evento);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                data.setText(day + " de " + ViewSharedMethods.getNomMes(month+1, getApplicationContext()) + " de " + year);
                Date d = new Date(year-1900, month, day);
                dataIni = d;
            }
        });
        calendarFinal = (CalendarView) findViewById(R.id.calendarViewFinal);
        calendarFinal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                dataFinal.setText(day + " de " + ViewSharedMethods.getNomMes(month+1, getApplicationContext()) + " de " + year);
                Date d = new Date(year-1900, month, day);
                dataFi = d;
            }
        });

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        preuText = (EditText) findViewById(R.id.editorPrecio);
        hora = (EditText) findViewById(R.id.horaApertura);
        horaFi = (EditText) findViewById(R.id.horaCierre);
        preuText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        hora.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        horaFi.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        data = (EditText) findViewById(R.id.editorFecha);
        dataFinal = (EditText) findViewById(R.id.editorFechaFinal);

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


        imgV = (ImageView) findViewById(R.id.imagenEvento);

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.profile)
                + '/' + getResources().getResourceTypeName(R.drawable.profile) + '/' + getResources().getResourceEntryName(R.drawable.profile) );

        imgV.setImageURI(uri);
        try {
            isImagen = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void crearEvento(View _view) {
        nomEvent = (EditText) findViewById(R.id.editorNEvento);
        descripcio = (EditText) findViewById(R.id.editorDescr);
        url = (EditText) findViewById(R.id.editorEntradas);
        localitzacio = (EditText) findViewById(R.id.editorLugar);
        gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        preuText = (EditText) findViewById(R.id.editorPrecio);
        hora = (EditText) findViewById(R.id.horaApertura);
        horaFi = (EditText) findViewById(R.id.horaCierre);
        data = (EditText) findViewById(R.id.editorFecha);
        dataFinal = (EditText) findViewById(R.id.editorFechaFinal);
        if (nomEvent.getText().toString().equals("") ||
                localitzacio.getText().toString().equals("") ||
                    !gratuit.isChecked() && preuText.getText().toString().equals("") ||
                        data.getText().toString().equals("") || hora.getText().toString().equals("") ||
                            horaFi.getText().toString().equals("")) {

            Toast.makeText(this, R.string.ERROR, Toast.LENGTH_SHORT).show();
        }
        else {
            if (!gratuit.isChecked() && !preuText.getText().toString().matches("[-+]?\\d*\\.?\\d+")) { //preu no és numéric
                Toast.makeText(this, R.string.ERROR_PRECIO, Toast.LENGTH_SHORT).show();
            }
            else {
                if (image != null) {
                    ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 75, bYtE);
                    image.recycle();
                }
                Long aux = null;
                if (dataFinal.getText().toString().equals(""))
                    aux = añadirHoraADate(dataIni, horaFi.getText().toString());
                else aux = añadirHoraADate(dataFi, horaFi.getText().toString());
                Long aux2 = añadirHoraADate(dataIni, hora.getText().toString());
                if (aux != null && aux2 != null) {
                    if (esHoraCorrecta(hora.getText().toString(),horaFi.getText().toString())) {
                        if (aux2 < aux) {
                            EventoEntity ee = new EventoEntity(nomEvent.getText().toString(),
                                    descripcio.getText().toString(),
                                    preuText.getText().toString(),
                                    url.getText().toString(),
                                    localitzacio.getText().toString(), lat, lng,
                                    Long.toString(aux2),
                                    Long.toString(aux),
                                    idGrupo
                            );
                            eMGR = MGRFactory.getInstance().getEventoMGR();
                            eMGR.crearConRedireccion(this,ee,isImagen);
                            Toast.makeText(this, R.string.DEFAULT_EVENTO_CREADO, Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(CrearEventoActivity.this, MainActivity.class));
                        } else Toast.makeText(this, R.string.ERROR_DIA, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(this, R.string.ERROR_HORAS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean esHoraCorrecta(String _hI, String _hF) {
        String[] timeI = _hI.split(":");
        String[] timeF = _hF.split(":");
        int horaIni = Integer.parseInt(timeI[0]);
        int minutoIni = Integer.parseInt(timeI[1]);
        int horaFi = Integer.parseInt(timeF[0]);
        int minutoFi = Integer.parseInt(timeF[1]);
        if (horaIni < horaFi) return true;
        else if (horaIni == horaFi) {
            if (minutoIni < minutoFi) return true;
            else return false;
        }
        else return false;
    }

    public Long añadirHoraADate(Date _d, String _s) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(_d); // sets calendar time/date
        Long result = null;
        String[] time = _s.split(":");
        if (time.length != 2) Toast.makeText(this, R.string.ERROR_HORA, Toast.LENGTH_SHORT).show();
        else {
            if (time[0].matches("[-+]?\\d*\\.?\\d+") && time[1].matches("[-+]?\\d*\\.?\\d+")) {
                int hora = Integer.parseInt(time[0]);
                int minuto = Integer.parseInt(time[1]);
                if ((hora < 0 || hora > 23) || (minuto < 0 || minuto > 59)) {
                    Toast.makeText(this, R.string.ERROR_HORA, Toast.LENGTH_SHORT).show();
                } else {
                    cal.add(Calendar.HOUR, hora); // adds one hour
                    cal.add(Calendar.MINUTE, minuto); // adds one hour
                    result = cal.getTime().getTime(); // returns new date object, one hour in the future
                }
            }
            else Toast.makeText(this, R.string.ERROR_HORA, Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void comprovarCheckBox(View _view) {
        if (gratuit.isChecked()) {
            preuText.setFocusable(false);
            preuText.setText("");
            preuText.setHint(getString(R.string.ESCRIBE_PRECIO_EVENTO));
        } else {
            preuText.setFocusableInTouchMode(true);
            preuText.setFocusable(true);
        }
    }

    public void redirecionarConIdEvento(String idEvento) {
        startActivity(new Intent(CrearEventoActivity.this, IndicarTagsActivity.class).putExtra("idEvento", idEvento));
    }


    //---------------------- GOOGLE PLACES API ---------------

    public void abrirGaleria(View _view) {
        Intent galeria = new Intent(Intent.ACTION_PICK);
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String dirGaleria = directorio.getPath();
        Uri data = Uri.parse(dirGaleria);

        galeria.setDataAndType(data, Constantes.SELECT_ALL_IMAGES); //get all image types

        startActivityForResult(galeria, GALERIA_REQUEST); //image return
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        if (_resultCode == RESULT_OK) {
            if (_requestCode == GALERIA_REQUEST) {
                Uri imageUri = _data.getData();

                try {
                    isImagen = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    //show the image to the user
                    imgV.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.ERROR_OBRIR_IMATGE, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void redireccionarConIdEvento(String id) {
        startActivity(new Intent(CrearEventoActivity.this, VerInfoEventoActivity.class).putExtra("key", id));
    }

    public void addEventoAlGrupo(String idEvento) {
        Map<String,String> idEventosAux = grupo.getIdEventos();
        if (idEventosAux == null) idEventosAux = new HashMap<String, String>();
        idEventosAux.put(idEvento, nomEvent.getText().toString());
        grupo.setIdEventos(idEventosAux);
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        System.out.println("actualitzare el grup: " + idGrupo);
        gMGR.actualizar(idGrupo, grupo);
    }
}
