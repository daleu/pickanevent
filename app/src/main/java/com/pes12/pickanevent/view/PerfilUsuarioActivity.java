package com.pes12.pickanevent.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AlertDialogManager;
import com.pes12.pickanevent.business.ConnectionDetector;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.ImagenPerfilUsuario.ImagenPerfilUsuarioMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

//--------- API TWITTER ---------
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
//-------------------------------

import static com.pes12.pickanevent.view.CrearEventoActivity.GALERIA_REQUEST;

/**
 * Created by Legault on 25/11/2016.
 */

public class PerfilUsuarioActivity extends BaseActivity {

    EditText nombre;
    EditText bio;
    TextView correo;
    ImageView foto;

    Bitmap image;

    // ---------- API TWITTER -----------------
    static String TWITTER_CONSUMER_KEY = "PUlLyuMrqQzt61r7dmHgy6b6W";
    static String TWITTER_CONSUMER_SECRET = "EoOyglsIzCZZJ4ghHBU2ZoLgUduoPEGYuSy1mZZmrI7IjlVigQ";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "x-oauthflow-twitter://twitterlogin";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Login button
    Button btnLoginTwitter;
    // Logout button
    Button btnLogoutTwitter;

    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Internet Connection detector
    private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // --------------------------------------------------------------------

    UsuarioMGR uMGR;
    ImagenPerfilUsuarioMGR iMGR;

    FirebaseUser current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);


        //Inicializaciones
        nombre = (EditText) findViewById(R.id.nickName);
        foto = (ImageView) findViewById(R.id.imagen);
        correo = (TextView) findViewById(R.id.correo);
        bio  = (EditText) findViewById(R.id.bio);
        ///////////////////////////////////////////////////////////////////////
        current = getAuth().getCurrentUser();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();

        iMGR = MGRFactory.getInstance().getImagenPerfilUsuarioMGR();


        mostrarInfoUsuario();

        //-------------- API TWITTER --------------
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if twitter keys are set
        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
            // Internet Connection is not present
            alert.showAlertDialog(this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
            // stop executing code by return
            return;
        }

        // All UI elements
        btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
        btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);

        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);

        /**
         * Twitter login button click event will call loginToTwitter() function
         * */
        btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Call login twitter function
                new LoginTask().execute();
            }
        });

        /** This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);

                    // Shared Preferences
                    SharedPreferences.Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.apply(); // save changes

                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                    // Hide login button
                    btnLoginTwitter.setVisibility(View.GONE);

                    // Show Update Twitter
                    btnLogoutTwitter.setVisibility(View.VISIBLE);

                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();

                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }

        // ----------------------------------------------

    }

    class LoginTask extends AsyncTask<Void,Void,String> {
        protected void onPostExecute(Bitmap result) {

        }

        @Override
        protected String doInBackground(Void... params) {
            loginToTwitter();
            return null;
        }
    }

    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    getString(R.string.ERROR_TWITTER_LOGUEADO), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, true);
    }

    public void mostrarInfoUsuario() {
        nombre.setText(getUsuarioActual().getNickname());
        bio.setText(getUsuarioActual().getBio());
        correo.setText(current.getEmail());
        //foto.setImageURI(current.getPhotoUrl());
        Picasso.with(this).load(current.getPhotoUrl()).into(foto);
        System.out.println(current.getPhotoUrl());

    }

    public void guardarInfo(View _view)
    {
        UsuarioEntity u = getUsuarioActual();
        String nickCambiado = nombre.getText().toString();
        String bioCambiado = bio.getText().toString();
        if(!nickCambiado.equals(u.getNickname())) {
            u.setNickname(nombre.getText().toString());
        }
        if(!bioCambiado.equals(u.getBio())) {
            u.setBio(bio.getText().toString());
        }
        uMGR.actualizar(getAuth().getCurrentUser().getUid(), u);
        setUsuarioActual(u);
    }

    public void restartPassword(View _view)
    {
        getAuth().sendPasswordResetEmail(current.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PerfilUsuarioActivity.this, getString(R.string.EMAIL_ENVIADO), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

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
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);


                    iMGR.subirImagenPerfil(inputStream, current,PerfilUsuarioActivity.this);
                    //mostrarInfoUsuario();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.ERROR_OBRIR_IMATGE), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
