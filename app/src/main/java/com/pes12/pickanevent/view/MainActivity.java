package com.pes12.pickanevent.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.dao.Usuario.UsuarioDAO;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
BLOQUE DE TEST
*/

        UsuarioMGR uMGR= new UsuarioMGR();
        uMGR.printNicknames();
        System.out.println("FINAL");

        /*UsuarioEntity usuario = new UsuarioEntity();
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
}
