package com.pes12.pickanevent.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private boolean cm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/*
BLOQUE DE TEST
*/


        UsuarioMGR uMGR= MGRFactory.getInstance().getUsuarioMGR();
        //uMGR.getAllUsers(this);


        UsuarioEntity usuario1 = new UsuarioEntity();
        UsuarioEntity usuario2 = new UsuarioEntity();
        UsuarioEntity usuario3 = new UsuarioEntity();
        Map<String,Boolean> eventos = new HashMap<>();
        Map<String,Boolean> usuarios = new HashMap<>();

        Map<String,UsuarioEntity> user = new HashMap<>();

        eventos.put("-KUbD9XAGOYClVDTyI0H", true); //e1
        //usu1
        usuario1.setUsername("usu1");
        usuario1.setNickname("nickusu1");
        usuario1.setPassword("pass1");
        usuario1.setEmail("email1@asdas.com");
        usuario1.setIdEventos(eventos);

        eventos = new HashMap<>();
        eventos.put("-KV9U5W-oL1c7xF9fXt4",true); //e3
        //usu3
        usuario2.setUsername("usu3");
        usuario2.setNickname("nickusu3");
        usuario2.setPassword("pass3");
        usuario2.setEmail("email3@asdas.com");
        usuario2.setIdEventos(eventos);

        eventos.put("-KUavWyMfmX-uxtRqMo5",true); //e2
        //usu2
        usuario3.setUsername("usu2");
        usuario3.setNickname("nickusu2");
        usuario3.setPassword("pass2");
        usuario3.setEmail("email2@asdas.com");
        usuario3.setIdEventos(eventos);

        //usuarios.put(, true);
        //usuarios.put(, true);
        //usuario1.setIdUsuarios(usuarios);

        /*user.put("",usuario);

        user=uMGR.guardar(user);

        Log.e("Main Activity","ID despues de crear: "+user.entrySet().iterator().next().getKey());

        usuario.setNickname("holadola");

        user=uMGR.guardar(user);
        Log.e("Main Activity","ID despues de guardar: "+user.entrySet().iterator().next().getKey());*/


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

    public void goVerInfoGrupo(View view) {
        startActivity(new Intent(MainActivity.this, VerInfoGrupoActivity.class));
    }

    public void goVerInfoEvento(View view) {
        startActivity(new Intent(MainActivity.this, VerInfoEventoActivity.class));
    }

    public void goBuscar(View view) {
        startActivity(new Intent(MainActivity.this, BuscarActivity.class));
    }

    public void goCrearEvento(View view) {
        startActivity(new Intent(MainActivity.this, CrearEventoActivity.class));
    }

    public void goEditarEvento(View view) {
        startActivity(new Intent(MainActivity.this, EditarEventoActivity.class));
    }

    public void goLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void goOtroUsuario(View view) {
        startActivity(new Intent(MainActivity.this, VerInfoOtroUsuarioActivity.class));
    }

    public void showUsuarioActual(View view){
        if(getUsuarioActual()!=null) Toast.makeText(MainActivity.this, (CharSequence) getUsuarioActual().toString(), Toast.LENGTH_SHORT).show();
        else Toast.makeText(MainActivity.this, "No hay usuario conectado", Toast.LENGTH_SHORT).show();

    }

    public void goCrearUsuario(View view) {
        startActivity(new Intent(MainActivity.this, CrearUsuarioActivity.class));
    }

    public void goVerEventosUsuariosQueSigo (View view) {
        startActivity(new Intent(MainActivity.this, VerEventosUsuariosQueSigoActivity.class));
    }

    public void cambiarModo(View view){
        if (cm == true) {
            cm = false;
            Toast.makeText(this, "Modo Usuario", Toast.LENGTH_SHORT).show();
        }
        else {
            cm = true;
            Toast.makeText(this, "Modo CM", Toast.LENGTH_SHORT).show();
        }
    }

}
