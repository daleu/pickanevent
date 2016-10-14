package com.pes12.pickanevent.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.dao.Usuario.UsuarioDAO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsuarioDAO uDAO= new UsuarioDAO();

        uDAO.guardar();
    }
}
