package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.pes12.pickanevent.R.id.Primero;
import static com.pes12.pickanevent.R.id.Tags;

/**
 * Created by Jan on 08/12/2016.
 */

public class EditarGrupoActivity extends BaseActivity{
    public static final int GALERIA_REQUEST = 20;
    ImageView foto;
    EditText nombre;
    EditText descripcion;
    Bitmap image;
    GrupoMGR gMGR;
    private TagMGR tMGR;
    String idGrupo;
    GrupoEntity grupo;
    InputStream isImagen;
    private Boolean changed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_grupo);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        Bundle param = getIntent().getExtras();
        //idGrupo = "-K_pzKaJijDFG_eT0Zry";
       if(param.getString("key")!=null){
            idGrupo = param.getString("key");
        }

        gMGR = MGRFactory.getInstance().getGrupoMGR();
        gMGR.getInfoGrupoEditar(this,idGrupo);
        tMGR = MGRFactory.getInstance().getTagMGR();

        foto = (ImageView) findViewById(R.id.imagenGrupo);
        nombre = (EditText) findViewById(R.id.editorNGrupo);
        descripcion = (EditText) findViewById(R.id.editorDescrGrupo);

    }

    public void mostrarInfoGrupoEditar (GrupoEntity _ge) {
        grupo = _ge;

        Map<String, String> tagsMap = grupo.getIdTags();
        if (tagsMap == null) tagsMap = new LinkedHashMap<>();
        tagsMap.put(grupo.getidTagGeneral(), "blabla");
        tMGR.getInfoTagEditar(this, tagsMap);

        nombre.setText(_ge.getNombreGrupo());
        descripcion.setText(_ge.getDescripcion());

        //String img = _ge.getImagen();
        //Bitmap imgBM = StringToBitMap(img);
        //foto.setImageBitmap(imgBM);
        //foto.setScaleType(ImageView.ScaleType.FIT_XY);


        Picasso.with(this).load(_ge.getImagen()).into(foto);

    }

    public void mostrarTags(final ArrayList<String> info) {
        LinearLayout linearLayout = (LinearLayout) findViewById(Tags);
        TextView primero = (TextView) findViewById(Primero);
        if (info.size() > 0) {
            //int id = 0;
            primero.setText(info.get(0));
            primero.setPadding(3,2,3,2);
            primero.setTextColor(Color.rgb(100,100,100));
            primero.hasOnClickListeners();
            primero.setBackgroundColor(Color.rgb(130,255,130));
            //primero.setId(id);

            primero.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(EditarGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
                }
            });

            for (int i = 1; i < info.size(); ++i) {
                TextView siguiente = new TextView(this);
                siguiente.setText(info.get(i));
                siguiente.setPadding(3, 2, 3, 2);
                siguiente.setTextColor(Color.rgb(100,100,100));
                siguiente.hasOnClickListeners();
                siguiente.setBackgroundColor(Color.rgb(130,255,130));
                //RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params1.addRule(RelativeLayout.RIGHT_OF,id);
                //++id;
                //siguiente.setId(id);
                siguiente.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(EditarGrupoActivity.this, VerGruposConTagActivity.class).putExtra(TagEntity.ATTRIBUTES.NOMBRETAG.getValue(), info.get(0).toString()));
                    }
                });
                linearLayout.addView(siguiente);
            }
        }
    }

    /*private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }*/

    public void updateGrupo(View _view) {
        if (nombre.getText().toString().equals(""))
            Toast.makeText(EditarGrupoActivity.this, getString(R.string.INDICAR_NOMBRE_GRUPO),
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

            //GrupoEntity update = new GrupoEntity(nombreG, descripG); //falta arreglar tema imatge
            if (!nombreG.equals(grupo.getNombreGrupo()))grupo.setNombreGrupo(nombreG);
            if (!descripG.equals(grupo.getDescripcion()))grupo.setDescripcion(descripG);

            if (changed) MGRFactory.getInstance().getImagenGrupoMGR().subirImagenAlEditar(isImagen,grupo,idGrupo, this);
            else redireccionar();

            gMGR.actualizar(idGrupo,grupo);
            Toast.makeText(EditarGrupoActivity.this, getString(R.string.GRUPO_EDITADO),
                    Toast.LENGTH_SHORT).show();

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
                try {
                    changed = true;
                    isImagen = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    //show the image to the user
                    foto.setImageBitmap(image);
                    grupo.setImagen(image.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.ERROR_OBRIR_IMATGE), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void editarTags(View view) {
        startActivity(new Intent(EditarGrupoActivity.this, IndicarTagsActivity.class).putExtra("key", idGrupo));
    }

    public void redireccionar() {

        startActivity(new Intent(EditarGrupoActivity.this, VerInfoGrupoActivity.class).putExtra("key",idGrupo).putExtra("origen", "crear"));

    }
}