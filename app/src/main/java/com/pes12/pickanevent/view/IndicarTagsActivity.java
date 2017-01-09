package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterTags;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.IEstadoCheckBox;
import com.pes12.pickanevent.business.InfoTags;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class IndicarTagsActivity extends BaseActivity implements IEstadoCheckBox {

    ListView tags;

    TagMGR tMGR;
    UsuarioMGR uMGR;
    GrupoMGR gMGR;

    Boolean esCM;

    Map<String, String> mapIdTags;
    String idGrupo;
    ArrayList<InfoTags> info;
    GrupoEntity grupo;
    UsuarioEntity usuarioReg;
    String idUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicar_tags);

        ImageButton searchImage = (ImageButton) findViewById(R.id.searchact);
        if (searchImage!=null && getUsuarioActual().getCm()) searchImage.setVisibility(View.INVISIBLE);

        Button botonNuevo = (Button) findViewById(R.id.crearTag);
        TextView textoNuevoTag = (TextView) findViewById(R.id.textoNuevo);
        TextView textoMinimoTags = (TextView) findViewById(R.id.tagsMinimos);
        tags = (ListView) findViewById(R.id.eventos);

        final EditText tv = (EditText) findViewById(R.id.inputBusqueda);
        tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                if (_cs.toString().length() != 0) {
                    showProgressDialog();
                    tMGR.getTagsByName(IndicarTagsActivity.this, tv.getText().toString());
                } else {
                    for (int i = 0; i < info.size(); ++i) {
                        if (mapIdTags.containsKey(info.get(i).getIdTag())) {
                            info.get(i).setChecked(true);
                        }
                    }
                    AdapterTags adapterTags = new AdapterTags(IndicarTagsActivity.this, R.layout.vista_adapter_tags, info);
                    tags.setAdapter(adapterTags);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _arg0, int _arg1, int _arg2, int _arg3) {
            }

            @Override
            public void afterTextChanged(Editable _arg0) {
            }
        });
        esCM = false;
        if (getUsuarioActual() != null) esCM = getUsuarioActual().getCm();

        if (esCM) { //el usuario es CM: mostrar texto y boton superiores
            textoMinimoTags.setVisibility(View.INVISIBLE);
            Bundle b = getIntent().getExtras();
            idGrupo = b.getString("key");
            botonNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_nuevo_tag, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(IndicarTagsActivity.this);
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
                            tMGR.crearNuevoTag(IndicarTagsActivity.this, tag);
                            alert.hide();
                        }
                    });
                }
            });
        }

        else { //el usuario no es CM: no mostrar ni el texto ni el boton superiores
            botonNuevo.setVisibility(View.INVISIBLE);
            textoNuevoTag.setVisibility(View.INVISIBLE);

            if (getIntent().getExtras() != null) {
                usuarioReg = (UsuarioEntity) getIntent().getExtras().getSerializable("usuarioReg");
                idUsu = getIntent().getExtras().getString("keyUsuR");

            }

        }

        tMGR = MGRFactory.getInstance().getTagMGR();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        tMGR.getTodosLosTags(this);
    }

    public void mostrarTags(ArrayList<InfoTags> _info) {
        info = _info;
        if (!esCM) {
            if (getUsuarioActual() != null) mapIdTags = getUsuarioActual().getIdTags();
            else mapIdTags = new LinkedHashMap<>();
            if (mapIdTags == null) {
                mapIdTags = new LinkedHashMap<>();
            }
            tratarInfo();
        }
        else {
            gMGR.getGrupoParaTags(this, idGrupo);
        }
    }

    public void mostrarTagsGrupo(GrupoEntity _g) {
        grupo = _g;
        if (_g.getIdTags() != null) mapIdTags = _g.getIdTags();
        else mapIdTags = new LinkedHashMap<>();
        for (int i = 0; i < info.size(); ++i) {
            if ((info.get(i).getIdTag().equals(_g.getidTagGeneral()))) {
                info.remove(i);
            }
        }
        tratarInfo();
    }

    public void tratarInfo() {
        if (mapIdTags == null) {
            mapIdTags = new LinkedHashMap<>();
        }
        //bucle que cambiara el estado de los tags a true si ya estan relacionados con el grupo
        for (int i = 0; i < info.size(); ++i) {
            if (mapIdTags.containsKey(info.get(i).getIdTag())) {
                info.get(i).setChecked(true);
            }
        }

        AdapterTags adapterTags = new AdapterTags(IndicarTagsActivity.this, R.layout.vista_adapter_tags, info);
        tags.setAdapter(adapterTags);
    }

    public void actualizarPreferencias(View view) {
        if (!esCM && mapIdTags.size() < 3) {
            Toast.makeText(IndicarTagsActivity.this, getString(R.string.INDICA_TRES_TAGS), Toast.LENGTH_SHORT).show();
        }
        else if (!esCM){
            if (usuarioReg == null) {
                getUsuarioActual().setIdTags(mapIdTags);
                actualizarCurrentUser();

            }
            else {

                usuarioReg.setIdTags(mapIdTags);
                uMGR.actualizar(idUsu, usuarioReg);
                signOut();
            }
            startActivity(new Intent(IndicarTagsActivity.this, MainActivity.class));

        }
        else {
            grupo.setIdTags(mapIdTags);
            gMGR.actualizar(idGrupo, grupo);
            for (int i = 0; i < info.size(); ++i) {
                TagEntity tagAux = info.get(i).getEntity();
                if (info.get(i).getChecked()) {
                    if (tagAux.getIdGrupos() == null) {
                        Map<String,String> mapAux = new LinkedHashMap<>();
                        tagAux.setIdGrupos(mapAux);
                    }
                    tagAux.getIdGrupos().put(idGrupo, grupo.getNombreGrupo());
                }
                else {
                    if (tagAux.getIdGrupos() != null) tagAux.getIdGrupos().remove(idGrupo);
                }
                tMGR.actualizar(info.get(i).getIdTag(), tagAux);
                startActivity(new Intent(IndicarTagsActivity.this, VerInfoGrupoActivity.class).putExtra("key", idGrupo));
            }
        }
    }

    @Override
    public void actualizarChecked(InfoTags infoTag) {

        if (infoTag.getChecked()) { //se ha marcado -> deberemos añadirlo al map de seleccionados
            mapIdTags.put(infoTag.getIdTag(), infoTag.getNombreTag());
        }
        else { //se ha desmarcado -> deberemos eliminarlo del map de seleccionados
            mapIdTags.remove(infoTag.getIdTag());
            for (int i = 0; i < info.size(); ++i) {
                if (info.get(i).getIdTag().equals(infoTag.getIdTag())) {
                    info.get(i).setChecked(false);
                }
            }
        }
    }

    public void checkNuevoTag(String _id, String nombreTag) {
        mapIdTags.put(_id, nombreTag);
        grupo.setIdTags(mapIdTags);
        gMGR.actualizar(idGrupo, grupo);
        gMGR.getGrupoParaTags(this, idGrupo);
    }

    public void mostrarResultadosBusquedaTags(ArrayList<InfoTags> _info) {
        for (int i = 0; i < _info.size(); ++i) {
            if (mapIdTags.containsKey(_info.get(i).getIdTag())) {
                _info.get(i).setChecked(true);
            }
        }
        AdapterTags ale = new AdapterTags(IndicarTagsActivity.this, R.layout.vista_adapter_tags, _info);
        tags.setAdapter(ale);
    }

}
