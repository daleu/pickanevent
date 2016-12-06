package com.pes12.pickanevent.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

import java.util.ArrayList;

public class BuscarEventoActivity extends BaseActivity {

    EventoMGR eMGR;
    UsuarioMGR uMGR;
    GrupoMGR gMGR;
    final Context context = this;
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_buscar_evento);
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final EditText textoNombreEvento = (EditText) findViewById(R.id.textoNombreEvento);
        final EditText textoLugarEvento = (EditText) findViewById(R.id.textoLugarEvento);
        final EditText textoUsuario = (EditText) findViewById(R.id.textoUsuario);
        final EditText textoGrupo = (EditText) findViewById(R.id.textoGrupo);
        final Button buscarEvento = (Button) findViewById(R.id.buscarEvento);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,R.array.busqueda_array
                ,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id) {
                if (spinner2.getSelectedItem().toString().equals("Evento")) {
                    spinner.setVisibility(View.VISIBLE);
                    buscarEvento.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.eventos_array
                            , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id) {
                            if (spinner.getSelectedItem().toString().equals("Nombre")) {
                                textoNombreEvento.setVisibility(View.VISIBLE);
                                textoLugarEvento.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);

                            } else if (spinner.getSelectedItem().toString().equals("Lugar")) {
                                textoNombreEvento.setVisibility(View.INVISIBLE);
                                textoLugarEvento.setVisibility(View.VISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);

                            } else {
                                textoNombreEvento.setVisibility(View.INVISIBLE);
                                textoLugarEvento.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);
                            }
                        }

                        public void onNothingSelected(AdapterView<?> _parent) {

                        }
                    });
                }
                else if (spinner2.getSelectedItem().toString().equals("Usuario")) {
                    spinner.setVisibility(View.INVISIBLE);
                    buscarEvento.setVisibility(View.INVISIBLE);
                    textoNombreEvento.setVisibility(View.INVISIBLE);
                    textoLugarEvento.setVisibility(View.INVISIBLE);
                    textoUsuario.setVisibility(View.VISIBLE);
                    textoGrupo.setVisibility(View.INVISIBLE);
                    textoUsuario.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                            if (_cs.toString().length() != 0) {
                                showProgressDialog();
                                uMGR.getUsuariosByNombre(BuscarEventoActivity.this, textoUsuario.getText().toString());
                            } else {
                                ListView lista = (ListView) findViewById(R.id.lista);
                                lista.setAdapter(null);
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence _arg0, int _arg1, int _arg2, int _arg3) {
                        }

                        @Override
                        public void afterTextChanged(Editable _arg0) {
                        }

                    });
                }
                else {
                    spinner.setVisibility(View.INVISIBLE);
                    buscarEvento.setVisibility(View.INVISIBLE);
                    textoNombreEvento.setVisibility(View.INVISIBLE);
                    textoLugarEvento.setVisibility(View.INVISIBLE);
                    textoUsuario.setVisibility(View.INVISIBLE);
                    textoGrupo.setVisibility(View.VISIBLE);
                    textoGrupo.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                            if (_cs.toString().length() != 0) {
                                showProgressDialog();
                                gMGR.getGruposByNombre(BuscarEventoActivity.this, textoGrupo.getText().toString());
                            }
                            else {
                                ListView lista = (ListView) findViewById(R.id.lista);
                                lista.setAdapter(null);
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence _arg0, int _arg1, int _arg2, int _arg3) {
                        }

                        @Override
                        public void afterTextChanged(Editable _arg0) {
                        }

                    });
                }
            }

            public void onNothingSelected(AdapterView<?> _parent) {

            }
        });

    }

    public void buscarEvento(View _view) {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        eMGR = MGRFactory.getInstance().getEventoMGR();
        if(spinner.getSelectedItem().toString().equals("Nombre")) {
            EditText textoEvento = (EditText) findViewById(R.id.textoNombreEvento);
            if (textoEvento.getText().toString().equals(""))
                Toast.makeText(this, R.string.ERROR, Toast.LENGTH_LONG).show();
            else eMGR.getInfoEventoElegido(this, "titulo", textoEvento.getText().toString());
        }
        else if (spinner.getSelectedItem().toString().equals("Lugar")) {
            EditText textoLugar = (EditText) findViewById(R.id.textoLugarEvento);
            if (textoLugar.getText().toString().equals(""))
                Toast.makeText(this, R.string.ERROR, Toast.LENGTH_LONG).show();
            else eMGR.getInfoEventoElegido(this, "localizacion", textoLugar.getText().toString());
        }
    }

    public void mostrarInfoEventoElegido(ArrayList<EventoEntity> _eventos) {
        if (_eventos.isEmpty())
            Toast.makeText(this, R.string.EVENTO_NO_ENCONTRADO, Toast.LENGTH_LONG).show();
        else {
            for (int i = 0; i < _eventos.size(); ++i) {
                Toast.makeText(this, _eventos.get(i).getTitulo(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mostrarInfoGrupoElegido(ArrayList<Info> _info) {
        AdapterLista ale = new AdapterLista(BuscarEventoActivity.this, R.layout.vista_adapter_lista, _info);
        ListView lista = (ListView) findViewById(R.id.lista);
        lista.setAdapter(ale);
    }

    public void mostrarInfoUsuarioElegido(ArrayList<Info> _info) {
        AdapterLista ale = new AdapterLista(BuscarEventoActivity.this, R.layout.vista_adapter_lista, _info);
        ListView lista = (ListView) findViewById(R.id.lista);
        lista.setAdapter(ale);
    }

}
