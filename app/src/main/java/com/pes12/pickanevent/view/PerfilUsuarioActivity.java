package com.pes12.pickanevent.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.ImagenPerfilUsuario.ImagenPerfilUsuarioMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.fabric.sdk.android.Fabric;

import static com.pes12.pickanevent.view.CrearEventoActivity.GALERIA_REQUEST;

/**
 * Created by Legault on 25/11/2016.
 */

public class PerfilUsuarioActivity extends BaseActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "PUlLyuMrqQzt61r7dmHgy6b6W";
    private static final String TWITTER_SECRET = "EoOyglsIzCZZJ4ghHBU2ZoLgUduoPEGYuSy1mZZmrI7IjlVigQ";

    EditText nombre;
    EditText bio;
    TextView correo;
    ImageView foto;

    Bitmap image;

    UsuarioMGR uMGR;
    ImagenPerfilUsuarioMGR iMGR;

    FirebaseUser current;

    //Twitter
    private TwitterLoginButton loginButton;
    private Button logout;
    private Button borrarCuenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig),new TweetComposer());
        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        //Inicializaciones
        nombre = (EditText) findViewById(R.id.nickName);
        foto = (ImageView) findViewById(R.id.imagen);
        correo = (TextView) findViewById(R.id.correo);
        bio = (EditText) findViewById(R.id.bio);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        logout = (Button) findViewById(R.id.twitter_logout_button);
        ///////////////////////////////////////////////////////////////////////
        current = getAuth().getCurrentUser();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();

        iMGR = MGRFactory.getInstance().getImagenPerfilUsuarioMGR();


        mostrarInfoUsuario();

        if (Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {
            loginButton.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                loginButton.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        //Boton eliminar cuenta
        borrarCuenta = (Button) findViewById(R.id.borrarCuenta);
        borrarCuenta.setOnClickListener(new View.OnClickListener() {
            Boolean esCM = getUsuarioActual().getCm();

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = null;
                if (getUsuarioActual().getCm())
                    dialoglayout = inflater.inflate(R.layout.dialog_borrar_cm, null);
                else
                    dialoglayout = inflater.inflate(R.layout.dialog_borrar_usuario, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilUsuarioActivity.this);
                builder.setView(dialoglayout);
                final AlertDialog alert = builder.create();
                alert.show();
                Button aceptar = (Button) dialoglayout.findViewById(R.id.borrarCuenta);
                Button cancelar = (Button) dialoglayout.findViewById(R.id.funcionVacia);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean noError = borrarCurrentUser();
                        String msg = noError ? getString(R.string.BORRADO_CUENTA_CORRECTO) : getString(R.string.ERROR_BORRAR);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        alert.hide();
                        if (noError) signOut();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.hide();
                    }
                });
            }
        });
    }

    public void logoutTwitter(View _view) {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            ClearCookies(getApplicationContext());
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
            loginButton.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }
    }

    public static void ClearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
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
        u.setUrlPhoto(current.getPhotoUrl().toString());
        uMGR.actualizar(getAuth().getCurrentUser().getUid(), u);
        setUsuarioActual(u);
        Toast.makeText(PerfilUsuarioActivity.this, "Datos Guardados", Toast.LENGTH_SHORT).show();
    }

    public void restartPassword(View _view)
    {
        getAuth().sendPasswordResetEmail(current.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PerfilUsuarioActivity.this, "Email enviado.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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


                    iMGR.subirImagenPerfil(inputStream, current,PerfilUsuarioActivity.this);
                    //mostrarInfoUsuario();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "No s'ha pogut obrir la imatge", Toast.LENGTH_LONG).show();
                }
            }
        }
        loginButton.onActivityResult(_requestCode, _resultCode, _data);
    }

    public void editarPreferencias (View view) {
        startActivity(new Intent(PerfilUsuarioActivity.this, IndicarTagsActivity.class));
    }

    //se tiene que poner para evitar que al volver de la edicion de tags se quede bloqueado si poder volver hacia atras
    @Override
    public void onBackPressed() {
        finish();
    }
}
