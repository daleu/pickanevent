package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CrearGrupoActivity extends BaseActivity {

    public static final int GALERIA_REQUEST = 20;
    ImageView foto;
    EditText nombre;
    EditText descripcion;
    EditText tagPrincipal;
    Bitmap image;
    GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        gMGR = MGRFactory.getInstance().getGrupoMGR();

        foto = (ImageView) findViewById(R.id.imagenGrupo);
        nombre = (EditText) findViewById(R.id.editorNGrupo);
        descripcion = (EditText) findViewById(R.id.editorDescrGrupo);
        tagPrincipal = (EditText) findViewById(R.id.editorTagPrincipal);
    }

    public void crearGrupo(View _view) {
        if (nombre.getText().toString().equals(""))
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.INDICAR_NOMBRE_GRUPO),
                    Toast.LENGTH_SHORT).show();
        else if (tagPrincipal.getText().toString().equals(""))
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.INDICAR_TAG_GRUPO),
                    Toast.LENGTH_SHORT).show();
        else {
            String imatge;
            if (image != null) {
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 75, bYtE);
                image.recycle();
                byte[] byteArray = bYtE.toByteArray();
                imatge = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else imatge = null;

            String nombreG = nombre.getText().toString();
            String descripG = descripcion.getText().toString();
            GrupoEntity nuevoGrupo = new GrupoEntity(nombreG, descripG, imatge, null, "-KW3Au4_Mb-w4hnq3rSm", null, null);
            gMGR.crear(nuevoGrupo);
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.DEFAULT_GRUPO_CREADO),
                    Toast.LENGTH_SHORT).show();

            //petaba con el setContentView
            //setContentView(R.layout.activity_main);
            startActivity(new Intent(CrearGrupoActivity.this, MainActivity.class));

        }
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
                    image = BitmapFactory.decodeStream(inputStream);
                    //show the image to the user
                    foto.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.ERROR_OBRIR_IMATGE, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
