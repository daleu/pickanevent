package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicar_tags);

        Button botonNuevo = (Button) findViewById(R.id.crearTag);
        TextView textoNuevoTag = (TextView) findViewById(R.id.textoNuevo);
        tags = (ListView) findViewById(R.id.eventos);

        esCM = getUsuarioActual().getCm();
        if (esCM) { //el usuario es CM: mostrar texto y boton superiores
            Bundle b = getIntent().getExtras();
            idGrupo = b.getString("idGrupo");
            botonNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_nuevo_tag, null);

                    Button crear = (Button) dialoglayout.findViewById(R.id.newTag);
                    final EditText nombreTag = (EditText) dialoglayout.findViewById(R.id.nombre);
                    crear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TagEntity tag = new TagEntity();
                            tag.setNombreTag(nombreTag.getText().toString());
                            tMGR.crear(tag);
                        }
                    });

                    AlertDialog.Builder builder = new AlertDialog.Builder(IndicarTagsActivity.this);
                    builder.setView(dialoglayout);
                    builder.show();
                }
            });
        } else { //el usuario no es CM: no mostrar ni el texto ni el boton superiores
            botonNuevo.setVisibility(View.INVISIBLE);
            textoNuevoTag.setVisibility(View.INVISIBLE);
        }

        tMGR = MGRFactory.getInstance().getTagMGR();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        tMGR.getTodosLosTags(this);
    }

    public void mostrarTags(ArrayList<InfoTags> _info) {
        info = _info;
        if (!esCM) {
            mapIdTags = getUsuarioActual().getIdTags();
            tratarInfo(mapIdTags);
        } else {
            gMGR.getGrupoParaTags(this, idGrupo);
        }
    }

    public void mostrarTagsGrupo(GrupoEntity g) {
        mapIdTags = g.getIdTags();
        tratarInfo(mapIdTags);
    }

    public void tratarInfo(Map<String, String> _mapIdTags) {
        if (_mapIdTags == null) {
            _mapIdTags = new LinkedHashMap<>();
        }
        //bucle que cambiara el estado de los tags a true si ya estan relacionados con el grupo
        for (int i = 0; i < info.size(); ++i) {
            if (_mapIdTags.containsKey(info.get(i).getIdTag())) {
                info.get(i).setChecked(true);
            }
        }

        AdapterTags adapterTags = new AdapterTags(IndicarTagsActivity.this, R.layout.vista_adapter_tags, info);
        tags.setAdapter(adapterTags);
    }

    public void actualizarPreferencias(View view) {
        if (!esCM && mapIdTags.size() < 3) {
            Toast.makeText(IndicarTagsActivity.this, "Indica un mínimo de 3 tags", Toast.LENGTH_SHORT).show();
        } else if (esCM) { //para un grupo solo es obligatorio el tag principal

        } else {
            getUsuarioActual().setIdTags(mapIdTags);
            actualizarUsuario();
        }
    }

    @Override
    public void actualizarChecked(InfoTags infoTag) {

        if (infoTag.getChecked()) { //se ha marcado -> deberemos añadirlo al map de seleccionados
            mapIdTags.put(infoTag.getIdTag(), infoTag.getNombreTag());
        } else { //se ha desmarcado -> deberemos eliminarlo del map de seleccionados
            mapIdTags.remove(infoTag.getIdTag());
        }

    }

}
