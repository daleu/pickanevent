package com.pes12.pickanevent.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(MainActivity.this, VerInfoGrupo.class));
/*
BLOQUE DE TEST
*/
/*
        UsuarioMGR uMGR= new UsuarioMGR(this);



        UsuarioEntity usuario = new UsuarioEntity();
        Map<String,Boolean> relaciones = new HashMap<>();
        Map<String,UsuarioEntity> user = new HashMap<>();

        relaciones.put("key1",true);
        relaciones.put("key2",true);
        relaciones.put("key3",true);

        usuario.setUsername("TestDeDAO");
        usuario.setNickname("SoyUnTest");
        usuario.setPassword("pass123");
        usuario.setEmail("asd@asdas.com");
        usuario.setIdEventos(relaciones);
        usuario.setIdGrupos(relaciones);
        usuario.setIdTags(relaciones);

        user.put("",usuario);

        user=uMGR.guardar(user);

        Log.e("Main Activity","ID despues de crear: "+user.entrySet().iterator().next().getKey());

        usuario.setNickname("holadola");

        user=uMGR.guardar(user);
        Log.e("Main Activity","ID despues de guardar: "+user.entrySet().iterator().next().getKey());

*/
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
