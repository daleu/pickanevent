package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterListaEventos;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.Map;

public class BuscarActivity extends BaseActivity {

    private UsuarioMGR uMGR;
    private GrupoMGR gMGR;
    private TextView tv;
    private TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        tv2 = (TextView)findViewById(R.id.texto2);
        tv = (TextView)findViewById(R.id.texto);
        //uMGR= UsuarioMGR.getInstance(); VIEJA
        uMGR = MGRFactory.getInstance().getUsuarioMGR();


        //gMGR= GrupoMGR.getInstance();
        gMGR = MGRFactory.getInstance().getGrupoMGR();
        final EditText tv = (EditText)findViewById(R.id.inputBusqueda);
        tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if(cs.toString().length()!=0)
                {
                    showProgressDialog();
                    uMGR.getUsersByUsername(BuscarActivity.this, tv.getText().toString());
                    gMGR.getGruposByNombreGrupo(BuscarActivity.this, tv.getText().toString());
                }

                else{

                    tv2.setText("");
                    tv.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

    }

    public void buscar(View view)
    {
        EditText tv = (EditText)findViewById(R.id.inputBusqueda);

        uMGR.getUsersByUsername(this,tv.getText().toString());
    }

    //funcion para probar lecturas
    public void printNicknames(ArrayList<Info> info) {

        tv.setText("Usuarios");
        ListView lv  =(ListView)findViewById(R.id.lvUsers);

        //arraylist Append
        AdapterListaEventos ale = new AdapterListaEventos(BuscarActivity.this,R.layout.vista_evento_en_lista,info);
        lv.setAdapter(ale);
    }

    public void printNombresGrupo(ArrayList<Info> info) {
        tv2.setText("Grupos");

        ListView lv  =(ListView)findViewById(R.id.lvGrupos);

        //arraylist Append
        AdapterListaEventos ale = new AdapterListaEventos(BuscarActivity.this,R.layout.vista_evento_en_lista,info);
        lv.setAdapter(ale);
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
