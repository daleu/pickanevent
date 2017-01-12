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
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.PlaceAutocompleteAdapter;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import static com.pes12.pickanevent.R.layout.activity_editar_evento;

public class EditarEventoActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText preuText;
    EditText hora;
    EditText horaFi;
    EditText data;
    EditText dataFinal;
    CheckBox gratuit;
    CalendarView calendar;
    CalendarView calendarFinal;
    ImageView foto;

    EditText nomEvent;
    EditText descripcio;
    EditText url;
    EditText localitzacio;

    String idGrupo;

    private Boolean changed = false;
    private Date dataIni;
    private Date dataFi;
    public static final int GALERIA_REQUEST = 20;
    private static final LatLngBounds bounds =
            new LatLngBounds(new LatLng(35.871045, -9.919695), new LatLng(42.957396, 4.729860));
    //------------------- GOOGLE PLACES API ------------------
    protected GoogleApiClient mGoogleApiClient;
    EventoMGR eMGR;
    Bitmap image;
    String lat;
    String lng;
    String idEvento;
    EventoEntity evento;
    InputStream is;
    Bundle param;

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

        //-------------- GOOGLE PLACES API -------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Places.GEO_DATA_API)
                .build();
        //-----------------------------------------------

        setContentView(activity_editar_evento);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        inicialitza();

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

        //--------------------------------------------------------

        idEvento = getIntent().getExtras().getString("key");

        eMGR = MGRFactory.getInstance().getEventoMGR();
        eMGR.getInfoEventoEditar(this,idEvento);
    }

    public void mostrarInfoEventoEditar(EventoEntity evento) {
        idGrupo = evento.getIdGrup();
        this.evento = evento;
        inicialitza();

        nomEvent.setText(evento.getTitulo());
        descripcio.setText(evento.getDescripcion());
        /*String img = evento.getImagen();
        Bitmap imgBM = StringToBitMap(img);
        foto.setImageBitmap(imgBM);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);*/
        Date dataI = evento.getDataInDate();
        Date dataF = evento.getDataFiDate();
        dataIni = dataI;
        dataFi = dataF;
        lat = evento.getLatitud();
        lng = evento.getLongitud();
        Calendar c = Calendar.getInstance();
        c.setTime(dataI);
        data.setText(c.get(Calendar.DAY_OF_MONTH) + " de " + ViewSharedMethods.getNomMes(
                dataI.getMonth()+1, getApplicationContext()) + " de " + (dataI.getYear()+1900));
        c.setTime(dataF);
        dataFinal.setText(c.get(Calendar.DAY_OF_MONTH) + " de " + ViewSharedMethods.getNomMes(
                dataF.getMonth()+1, getApplicationContext()) + " de " + (dataF.getYear()+1900));
        if (dataI.getHours() < 10) {
            if (dataI.getMinutes() < 10) hora.setText("0" + dataI.getHours() + ":0" + dataI.getMinutes());
            else hora.setText("0" + dataI.getHours() + ":" + dataI.getMinutes());
        }
        else {
            if (dataI.getMinutes() < 10) hora.setText(dataI.getHours() + ":0" + dataI.getMinutes());
            else hora.setText(dataI.getHours() + ":" + dataI.getMinutes());
        }
        if (dataF.getHours() < 10) {
            if (dataF.getMinutes() < 10) horaFi.setText("0" + dataF.getHours() + ":0" + dataF.getMinutes());
            else horaFi.setText("0" + dataF.getHours() + ":" + dataF.getMinutes());
        }
        else {
            if (dataF.getMinutes() < 10) horaFi.setText(dataF.getHours() + ":0" + dataF.getMinutes());
            else horaFi.setText(dataF.getHours() + ":" + dataF.getMinutes());
        }
        if (evento.getPrecio().equals("")) {
            gratuit.setChecked(true);
            preuText.setFocusable(false);
            preuText.setText("");
            preuText.setHint(getString(R.string.ESCRIBE_PRECIO_EVENTO));
        } else {
            gratuit.setChecked(false);
            preuText.setText(evento.getPrecio());
        }
        localitzacio.setText(evento.getLocalizacion());
        url.setText(evento.getWebpage());

        if (evento.getImagen() != null)Picasso.with(this).load(evento.getImagen()).into(foto);
        else {
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.photo_not_available)
                    + '/' + getResources().getResourceTypeName(R.drawable.photo_not_available) + '/' + getResources().getResourceEntryName(R.drawable.photo_not_available) );

            foto.setImageURI(uri);
        }

    }

    private void inicialitza() {
        nomEvent = (EditText) findViewById(R.id.editorNEvento);
        descripcio = (EditText) findViewById(R.id.editorDescr);
        url = (EditText) findViewById(R.id.editorEntradas);
        localitzacio = (EditText) findViewById(R.id.editorLugar);
        foto = (ImageView) findViewById(R.id.imagenEvento);
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
        gratuit = (CheckBox) findViewById(R.id.checkBoxGratis);
        preuText = (EditText) findViewById(R.id.editorPrecio);
        hora = (EditText) findViewById(R.id.horaApertura);
        horaFi = (EditText) findViewById(R.id.horaCierre);
        preuText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        data = (EditText) findViewById(R.id.editorFecha);
        dataFinal = (EditText) findViewById(R.id.editorFechaFinal);
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

    public void updateEvento(View _view) {
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
                    //if (esHoraCorrecta(hora.getText().toString(),horaFi.getText().toString())) {
                        if (aux2 < aux) {
                            System.out.println(nomEvent.getText().toString());
                            System.out.println(preuText.getText().toString());
                            System.out.println(Long.toString(aux2));
                            System.out.println(Long.toString(aux));
                            System.out.println(descripcio.getText().toString());
                            EventoEntity update = new EventoEntity(nomEvent.getText().toString(),
                                    descripcio.getText().toString(),
                                    preuText.getText().toString(),
                                    url.getText().toString(),
                                    localitzacio.getText().toString(), lat, lng,
                                    Long.toString(aux2),
                                    Long.toString(aux),
                                    idGrupo
                            );
                            //eMGR.actualizarConRedireccion(idEvento,this,update,is);

                            if (changed) MGRFactory.getInstance().getImagenEventoMGR().subirImagenAlEditar(is,update,idEvento, this);
                            else {
                                update.setImagen(evento.getImagen());
                                redireccionar();
                            }

                            eMGR.actualizar(idEvento, update);
                            Toast.makeText(EditarEventoActivity.this, getString(R.string.EVENTO_EDITADO),
                                    Toast.LENGTH_SHORT).show();

                            //startActivity(new Intent(EditarEventoActivity.this, NavigationDrawer.class));
                        } else Toast.makeText(this, R.string.ERROR_DIA, Toast.LENGTH_SHORT).show();
                    //}
                    //else Toast.makeText(this, R.string.ERROR_HORAS, Toast.LENGTH_SHORT).show();
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
                if ((hora < 0 && hora > 23) || (minuto < 0 && minuto > 59)) {
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

    public void redireccionarConIdEvento(String idEvento) {
        startActivity(new Intent(EditarEventoActivity.this, VerInfoEventoActivity.class).putExtra("key", idEvento));
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
                    changed = true;
                    is = getContentResolver().openInputStream(imageUri);
                    ImageView imgV = (ImageView) findViewById(R.id.imagenEvento);
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

    public void redireccionar() {
        startActivity(new Intent(EditarEventoActivity.this, VerInfoEventoActivity.class).putExtra("key", idEvento).putExtra("origen", "crear"));
    }
}
