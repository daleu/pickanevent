package com.pes12.pickanevent.view;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterTags;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;

import java.util.ArrayList;

public class IndicarTagsActivity extends BaseActivity {

    ListView tags;

    TagMGR tMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicar_tags);
        Button botonNuevo = (Button) findViewById(R.id.crearTag);
        TextView textoNuevoTag = (TextView) findViewById(R.id.textoNuevo);
        tags = (ListView) findViewById(R.id.eventos);

        if (getUsuarioActual().getCm()) { //el usuario es CM: mostrar texto y boton superiores

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
        }

        else { //el usuario no es CM: no mostrar ni el texto ni el boton superiores
            botonNuevo.setVisibility(View.INVISIBLE);
            textoNuevoTag.setVisibility(View.INVISIBLE);
        }

        tMGR = MGRFactory.getInstance().getTagMGR();
        tMGR.getTodosLosTags(this);
    }

    public void mostrarTags(ArrayList<String> info) {
        AdapterTags adapterTags = new AdapterTags(IndicarTagsActivity.this,R.layout.vista_adapter_tags,info);
        tags.setAdapter(adapterTags);

    }


}
