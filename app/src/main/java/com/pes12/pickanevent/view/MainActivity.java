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
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

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

/*
        UsuarioMGR uMGR= UsuarioMGR.getInstance();
        uMGR.getAllUsers(this);


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
