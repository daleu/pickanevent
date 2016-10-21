package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.Map;

public class Buscar extends AppCompatActivity {

    private UsuarioMGR uMGR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        uMGR= UsuarioMGR.getInstance();
        final EditText tv = (EditText)findViewById(R.id.inputBusqueda);
        tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.toString().length()!=0)uMGR.getUsersByUsername(Buscar.this,tv.getText().toString());
                else{
                    TextView tv = (TextView)findViewById(R.id.texto);
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
        tv.setText("");

        for (Map.Entry<String, UsuarioEntity> entry : hm.entrySet()) {
            tv.setText(tv.getText()+ "\r\n"+entry.getValue().getUsername());
            System.out.println("clave=" + entry.getKey() + ", nickanme=" + entry.getValue().toString());
        }
    }
}
