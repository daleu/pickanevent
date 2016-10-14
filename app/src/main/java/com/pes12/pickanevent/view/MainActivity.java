package com.pes12.pickanevent.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.dao.Usuario.UsuarioDAO;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
BLOQUE DE TEST
*/
        /*
        UsuarioDAO uDAO= new UsuarioDAO();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(2l);
        usuario.setUsername("TestDeDAO");
        usuario.setNickname("SoyUnTest");
        usuario.setPassword("pass123");
        usuario.setEmail("asd@asdas.com");
        uDAO.guardar(usuario);*/
    }
}
