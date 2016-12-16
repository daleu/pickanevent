package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;

import java.util.ArrayList;

public class BuscarActivity extends BaseActivity {

    private UsuarioMGR uMGR;
    private GrupoMGR gMGR;
    private TextView tv1;
    private TextView tv2;
    private ListView lvu;
    private ListView lvg;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        tv2 = (TextView) findViewById(R.id.texto2);
        tv1 = (TextView) findViewById(R.id.texto);
        lvu = (ListView) findViewById(R.id.lvUsers);
        lvg = (ListView) findViewById(R.id.lvGrupos);
        //uMGR= UsuarioMGR.getInstance(); VIEJA
        uMGR = MGRFactory.getInstance().getUsuarioMGR();


        //gMGR= GrupoMGR.getInstance();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        final EditText tv = (EditText) findViewById(R.id.inputBusqueda);
         tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence _cs, int _arg1, int _arg2, int _arg3) {

                if (_cs.toString().length() != 0) {
                    showProgressDialog();
                    uMGR.getUsersByNickname(BuscarActivity.this, tv.getText().toString());
                    gMGR.getGruposByNombreGrupo(BuscarActivity.this, tv.getText().toString());
                } else {
                    lvu.setAdapter(null);
                    lvg.setAdapter(null);
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


    //funcion para probar lecturas
    public void printNicknames(ArrayList<Info> _info) {

        tv1.setText(R.string.DEFAULT_USUARIOS);


        //arraylist Append
        AdapterLista ale = new AdapterLista(BuscarActivity.this, R.layout.vista_adapter_lista, _info);
        lvu.setAdapter(ale);
    }

    public void printNombresGrupo(ArrayList<Info> _info) {
        tv2.setText(R.string.DEFAULT_GRUPOS);


        //arraylist Append
        AdapterLista ale = new AdapterLista(BuscarActivity.this, R.layout.vista_adapter_lista, _info);
        lvg.setAdapter(ale);

    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
