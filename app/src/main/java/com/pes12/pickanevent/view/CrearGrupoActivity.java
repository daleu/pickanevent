package com.pes12.pickanevent.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterTags;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.IEstadoCheckBox;
import com.pes12.pickanevent.business.InfoTags;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CrearGrupoActivity extends BaseActivity implements IEstadoCheckBox {

    public static final int GALERIA_REQUEST = 20;
    ImageView foto;
    EditText nombre;
    EditText descripcion;
    EditText tagPrincipal;
    Bitmap image;
    GrupoMGR gMGR;
    TagMGR tMGR;
    ListView listaTags;
    ArrayList<InfoTags> info;
    InputStream isImagen;

    GrupoEntity nuevoGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        gMGR = MGRFactory.getInstance().getGrupoMGR();
        tMGR = MGRFactory.getInstance().getTagMGR();


        foto = (ImageView) findViewById(R.id.imagenGrupo);
        nombre = (EditText) findViewById(R.id.editorNGrupo);
        descripcion = (EditText) findViewById(R.id.editorDescrGrupo);
        tagPrincipal = (EditText) findViewById(R.id.editorTagPrincipal);
        listaTags = (ListView) findViewById(R.id.resultatTagPrin);

        final EditText tv = (EditText) findViewById(R.id.editorTagPrincipal);
        tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                if (_cs.toString().length() != 0) {
                    showProgressDialog();
                    tMGR.getTagsByName_principal(CrearGrupoActivity.this, tv.getText().toString());
                }
                else {
                    if (ultimoMarcado != null) {
                        for (int i = 0; i < info.size(); ++i) {
                            if (info.get(i).getIdTag().equals(ultimoMarcado.getIdTag())) {
                                info.get(i).setChecked(true);
                            }
                        }
                        AdapterTags ale = new AdapterTags(CrearGrupoActivity.this, R.layout.vista_adapter_tags, info);
                        listaTags.setAdapter(ale);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _arg0, int _arg1, int _arg2, int _arg3) {
            }

            @Override
            public void afterTextChanged(Editable _arg0) {
            }

        });

        Button botonNuevo = (Button) findViewById(R.id.crearTag);
        botonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_nuevo_tag, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(CrearGrupoActivity.this);
                builder.setView(dialoglayout);
                final AlertDialog alert =  builder.create();
                alert.show();
                Button crear = (Button) dialoglayout.findViewById(R.id.newTag);
                final EditText nombreTag = (EditText) dialoglayout.findViewById(R.id.nombre);
                crear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TagEntity tag = new TagEntity();
                        tag.setNombreTag(nombreTag.getText().toString());
                        tMGR.crear(tag);
                        alert.hide();
                    }
                });


            }
        });

        tMGR.getTodosLosTags_principal(this);

    }

    public void crearGrupo(View _view) {
        if (nombre.getText().toString().equals(""))
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.INDICAR_NOMBRE_GRUPO),
                    Toast.LENGTH_SHORT).show();
        else if (ultimoMarcado == null)
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.INDICAR_TAG_GRUPO),
                    Toast.LENGTH_SHORT).show();
        else {
            String nombreG = nombre.getText().toString();
            String descripG = descripcion.getText().toString();
            nuevoGrupo = new GrupoEntity(nombreG, descripG);
            /*ASIGNAR TAG PRINCIPAAAAL*/
            nuevoGrupo.setIdTagGeneral(ultimoMarcado.getIdTag());

            gMGR.crearConRedireccion(this, nuevoGrupo, isImagen);
            Toast.makeText(CrearGrupoActivity.this, getString(R.string.DEFAULT_GRUPO_CREADO),
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        if (_resultCode == RESULT_OK) {
            if (_requestCode == GALERIA_REQUEST) {
                Uri imageUri = _data.getData();

                try {
                    isImagen = getContentResolver().openInputStream(imageUri);
                    ImageView imgV = (ImageView) findViewById(R.id.imagenGrupo);
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

    public void abrirGaleria(View _view) {
        Intent galeria = new Intent(Intent.ACTION_PICK);
        File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String dirGaleria = directorio.getPath();
        Uri data = Uri.parse(dirGaleria);

        galeria.setDataAndType(data, Constantes.SELECT_ALL_IMAGES); //get all image types

        startActivityForResult(galeria, GALERIA_REQUEST); //image return
    }


    public void mostrarResultadosBusquedaTags(ArrayList<InfoTags> _info) {
        for (int i = 0; i < _info.size(); ++i) {
            if (ultimoMarcado != null && ultimoMarcado.getIdTag().equals(_info.get(i).getIdTag())) {
                _info.get(i).setChecked(true);
            }
        }
        info = _info;
        AdapterTags ale = new AdapterTags(CrearGrupoActivity.this, R.layout.vista_adapter_tags, _info);
        listaTags.setAdapter(ale);
    }

    InfoTags ultimoMarcado = null;

    @Override
    public void actualizarChecked(InfoTags infoTag) {
        //ELIMINAR TODOS LOS CHECKS MENOS EL QUE SE ACABA DE HACER
        if (infoTag.getChecked()) {
            ultimoMarcado = infoTag;
            for (int i = 0; i < info.size(); ++i) {
                if (info.get(i).checked) {
                    info.get(i).setChecked(false);
                }
            }
            //info.remove(infoTag);
            infoTag.setChecked(true);
            //info.add(infoTag);
            mostrarResultadosBusquedaTags(info);
        }
        else {
            ultimoMarcado = null;
        }
    }

    public void redireccionarConIdGrupo(String idGrupo) {
          /*ASIGNAR grupo al tag principal*/
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        if (ultimoMarcado.getEntity().getIdGrupos() == null) ultimoMarcado.getEntity().setIdGrupos(newMap);
        ultimoMarcado.getEntity().getIdGrupos().put(idGrupo, nuevoGrupo.getNombreGrupo());
        tMGR.actualizar(ultimoMarcado.getIdTag(), ultimoMarcado.getEntity());
        startActivity(new Intent(CrearGrupoActivity.this, IndicarTagsActivity.class).putExtra("key", idGrupo));
        //startActivity(new Intent(CrearGrupoActivity.this, VerInfoGrupoActivity.class).putExtra("key", idGrupo));
    }
}
