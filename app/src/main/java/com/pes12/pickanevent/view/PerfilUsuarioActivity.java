package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

    UsuarioMGR uMGR;
    ImagenPerfilUsuarioMGR iMGR;

    FirebaseUser current;

    //Twitter
    private TwitterLoginButton loginButton;


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

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
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
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

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
}
