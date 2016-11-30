package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.ImagenPerfilUsuario.ImagenPerfilUsuarioMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.pes12.pickanevent.view.CrearEventoActivity.GALERIA_REQUEST;

/**
 * Created by Legault on 25/11/2016.
 */

public class PerfilUsuarioActivity extends BaseActivity {

    TextView nombre;
    TextView correo;
    ImageView foto;

    Bitmap image;

    UsuarioMGR uMGR;
    ImagenPerfilUsuarioMGR iMGR;

    FirebaseUser current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);


        //Inicializaciones
        nombre = (TextView) findViewById(R.id.nickName);
        foto = (ImageView) findViewById(R.id.imagen);
        correo = (TextView) findViewById(R.id.correo);
        ///////////////////////////////////////////////////////////////////////
        current = getAuth().getCurrentUser();

        iMGR = MGRFactory.getInstance().getImagenPerfilUsuarioMGR();


        mostrarInfoUsuario();


    }

    public void mostrarInfoUsuario() {
        nombre.setText(getUsuarioActual().getNickname());
        correo.setText(current.getEmail());
        //foto.setImageURI(current.getPhotoUrl());
        Picasso.with(this).load(current.getPhotoUrl()).into(foto);
        System.out.println(current.getPhotoUrl());

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


                    iMGR.subirImagen(inputStream, current);
                    mostrarInfoUsuario();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "No s'ha pogut obrir la imatge", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
