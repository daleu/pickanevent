package com.pes12.pickanevent.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;

import java.util.ArrayList;
import java.util.Date;

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
        eMGR = MGRFactory.getInstance().getEventoMGR();
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        final EditText textoNombreEvento = (EditText) findViewById(R.id.textoNombreEvento);
        final EditText textoLugarEvento = (EditText) findViewById(R.id.textoLugarEvento);
        final EditText textoUsuario = (EditText) findViewById(R.id.textoUsuario);
        final EditText textoGrupo = (EditText) findViewById(R.id.textoGrupo);
        final ListView lista = (ListView) findViewById(R.id.lista);
        final ListView lista2 = (ListView) findViewById(R.id.lista2);
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
        final EditText data = (EditText) findViewById(R.id.textoDiaEvento);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,R.array.busqueda_array
                ,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id) {
                textoGrupo.setText(null);
                textoLugarEvento.setText(null);
                textoNombreEvento.setText(null);
                textoUsuario.setText(null);
                data.setText(null);
                if (spinner2.getSelectedItem().toString().equals("Evento")) {
                    lista.setVisibility(View.INVISIBLE);
                    lista2.setVisibility(View.VISIBLE);
                    lista2.setAdapter(null);
                    spinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.eventos_array
                            , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> _parent, View _view, int _pos, long _id) {
                            textoLugarEvento.setText(null);
                            textoNombreEvento.setText(null);
                            data.setText(null);
                            if (spinner.getSelectedItem().toString().equals("Nombre")) {
                                lista2.setAdapter(null);
                                textoNombreEvento.setVisibility(View.VISIBLE);
                                textoLugarEvento.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);
                                spinner3.setVisibility(View.INVISIBLE);
                                calendar.setVisibility(View.INVISIBLE);
                                data.setVisibility(View.INVISIBLE);
                                textoNombreEvento.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                                        if (_cs.toString().length() != 0) {
                                            showProgressDialog();
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "titulo", textoNombreEvento.getText().toString());
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

                            } else if (spinner.getSelectedItem().toString().equals("Lugar")) {
                                lista2.setAdapter(null);
                                textoNombreEvento.setVisibility(View.INVISIBLE);
                                textoLugarEvento.setVisibility(View.VISIBLE);
                                spinner3.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);
                                calendar.setVisibility(View.INVISIBLE);
                                data.setVisibility(View.INVISIBLE);
                                textoLugarEvento.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                                        if (_cs.toString().length() != 0) {
                                            showProgressDialog();
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "localizacion", textoLugarEvento.getText().toString());
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

                            } else if (spinner.getSelectedItem().toString().equals("Precio")) {
                                lista2.setAdapter(null);
                                textoNombreEvento.setVisibility(View.INVISIBLE);
                                textoLugarEvento.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);
                                spinner3.setVisibility(View.VISIBLE);
                                calendar.setVisibility(View.INVISIBLE);
                                data.setVisibility(View.INVISIBLE);
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.precio_array
                                        , android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adapter);
                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> _parent, View _view, int _position, long _id) {
                                        if (spinner3.getSelectedItem().toString().equals("Gratis")) {
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "precio", "0");
                                        }
                                        else if (spinner3.getSelectedItem().toString().equals("Menos de 50€")) {
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "precio", "50");
                                        }
                                        else if (spinner3.getSelectedItem().toString().equals("Entre 50€ y 200€")) {
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "precio", "50<>200");
                                        }
                                        else {
                                            eMGR.getInfoEventoElegido(BuscarEventoActivity.this,
                                                    "precio", ">200");
                                        }


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> _parent) {

                                    }
                                });

                            }
                            else {
                                lista2.setAdapter(null);
                                textoNombreEvento.setVisibility(View.INVISIBLE);
                                textoLugarEvento.setVisibility(View.INVISIBLE);
                                textoUsuario.setVisibility(View.INVISIBLE);
                                textoGrupo.setVisibility(View.INVISIBLE);
                                spinner3.setVisibility(View.INVISIBLE);
                                calendar.setVisibility(View.INVISIBLE);
                                data.setVisibility(View.VISIBLE);
                                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                    @Override
                                    public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                                        data.setText(day + " de " + ViewSharedMethods.getNomMes(month+1, getApplicationContext()) + " de " + year);
                                        calendar.setVisibility(View.INVISIBLE);
                                        Date d = new Date(year-1900, month, day);
                                        long l = d.getTime();
                                        eMGR.getInfoEventoElegido(BuscarEventoActivity.this, "dia", Long.toString(l));
                                    }
                                });

                                data.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        calendar.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }

                        public void onNothingSelected(AdapterView<?> _parent) {

                        }
                    });
                }
                else if (spinner2.getSelectedItem().toString().equals("Usuario")) {
                    lista2.setVisibility(View.INVISIBLE);
                    lista.setVisibility(View.VISIBLE);
                    lista.setAdapter(null);
                    spinner.setVisibility(View.INVISIBLE);
                    spinner3.setVisibility(View.INVISIBLE);
                    textoNombreEvento.setVisibility(View.INVISIBLE);
                    textoLugarEvento.setVisibility(View.INVISIBLE);
                    textoUsuario.setVisibility(View.VISIBLE);
                    textoGrupo.setVisibility(View.INVISIBLE);
                    calendar.setVisibility(View.INVISIBLE);
                    data.setVisibility(View.INVISIBLE);
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
                    lista2.setVisibility(View.INVISIBLE);
                    lista.setAdapter(null);
                    lista.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    spinner3.setVisibility(View.INVISIBLE);
                    textoNombreEvento.setVisibility(View.INVISIBLE);
                    textoLugarEvento.setVisibility(View.INVISIBLE);
                    textoUsuario.setVisibility(View.INVISIBLE);
                    textoGrupo.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.INVISIBLE);
                    data.setVisibility(View.INVISIBLE);
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

    public void mostrarInfoEventoElegido(ArrayList<Info> _info) {
        AdapterLista ale = new AdapterLista(BuscarEventoActivity.this, R.layout.vista_adapter_lista, _info);
        ListView lista2 = (ListView) findViewById(R.id.lista2);
        lista2.setAdapter(ale);
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
