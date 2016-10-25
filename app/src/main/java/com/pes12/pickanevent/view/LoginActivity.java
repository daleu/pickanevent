package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.EncodeUtil;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
BLOQUE DE TEST
*/


        //UsuarioMGR uMGR= UsuarioMGR.getInstance(); VIEJA
        UsuarioMGR uMGR = MGRFactory.getInstance().getUsuarioMGR(); //NUEVA

       UsuarioEntity usuario = new UsuarioEntity();
        Map<String,Boolean> relaciones = new HashMap<>();
        Map<String,UsuarioEntity> user = new HashMap<>();

        relaciones.put("key1",true);
        relaciones.put("key2",true);
        relaciones.put("key3",true);

        usuario.setUsername("PruebaDePassEncoded");
        usuario.setNickname("I'mATest");
        usuario.setPassword(EncodeUtil.encodePasswordSHA1("123123"));
        usuario.setEmail("asd@asdas.com");
        usuario.setIdEventos(relaciones);
        usuario.setIdGrupos(relaciones);
        usuario.setIdTags(relaciones);

        user.put("",usuario);

        user=uMGR.guardarUsuario(user);

        uMGR.login(this,"PruebaDePassEncoded","123123");

    }




}
