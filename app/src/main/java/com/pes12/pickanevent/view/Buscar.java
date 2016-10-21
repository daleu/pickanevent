package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.Map;

public class Buscar extends AppCompatActivity {

    private UsuarioMGR uMGR;
    private GrupoMGR gMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        uMGR= UsuarioMGR.getInstance();


        gMGR= GrupoMGR.getInstance();
        final EditText tv = (EditText)findViewById(R.id.inputBusqueda);
        tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if(cs.toString().length()!=0)
                {
                    uMGR.getUsersByUsername(Buscar.this, tv.getText().toString());
                    gMGR.getGruposByNombreGrupo(Buscar.this, tv.getText().toString());
                }

                else{
                    TextView tv = (TextView)findViewById(R.id.texto);
                    TextView tv2 = (TextView)findViewById(R.id.texto2);
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
    public void printNicknames(Map<String,UsuarioEntity> hm) {

        System.out.println("Mostrando los valores:");
        TextView tv = (TextView)findViewById(R.id.texto);
        tv.setText("Usuarios:\n");
        if(hm.entrySet().size()==0)tv.setText(tv.getText()+"No hay usuarios que coincidan.");
        for (Map.Entry<String, UsuarioEntity> entry : hm.entrySet()) {
            tv.setText(tv.getText()+entry.getValue().getUsername());
            System.out.println("clave=" + entry.getKey() + ", nickanme=" + entry.getValue().toString());
        }
    }

    public void printNombresGrupo(Map<String, GrupoEntity> hm) {


        System.out.println("Mostrando los valores:");
        TextView tv = (TextView)findViewById(R.id.texto2);
        tv.setText("\nGrupos:\r\n");
        if(hm.entrySet().size()==0)tv.setText(tv.getText()+"No hay grupos que coincidan.");
        for (Map.Entry<String, GrupoEntity> entry : hm.entrySet()) {
            tv.setText(tv.getText()+entry.getValue().getNombreGrupo());
            System.out.println("clave=" + entry.getKey() + ", nickanme=" + entry.getValue().toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
