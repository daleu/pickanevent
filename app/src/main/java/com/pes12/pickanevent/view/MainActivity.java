package com.pes12.pickanevent.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import java.util.Map;

public class MainActivity extends BaseActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "PUlLyuMrqQzt61r7dmHgy6b6W";
    private static final String TWITTER_SECRET = "EoOyglsIzCZZJ4ghHBU2ZoLgUduoPEGYuSy1mZZmrI7IjlVigQ";


    private boolean cm = false;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig),new TweetComposer());
        setContentView(R.layout.activity_main);

/*
BLOQUE DE TEST
*/

/*
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

        usuarios.put("-KWMemvaQzFXxDTfF4zG",true);
        usuarios.put("-KWMemvpLMu62EajFZ2b",true);
        uMGR.crear(usuario1);

        usuario1.setIdUsuarios(usuarios);
        //usuarios.put(, true);
        //usuarios.put(, true);
        //usuario1.setIdUsuarios(usuarios);

        user.put("",usuario);

        user=uMGR.guardar(user);

        Log.e("Main Activity","ID despues de crear: "+user.entrySet().iterator().next().getKey());

        usuario.setNickname("holadola");

        user=uMGR.guardar(user);
        Log.e("Main Activity","ID despues de guardar: "+user.entrySet().iterator().next().getKey());*/


    }

    //funcion para probar lecturas
    public void printNicknames(Map<String, UsuarioEntity> _hm) {

        System.out.println("Mostrando los valores:");
        TextView tv = (TextView) findViewById(R.id.texto);
        tv.setText("");

        for (Map.Entry<String, UsuarioEntity> entry : _hm.entrySet()) {
            tv.setText(tv.getText() + "\r\n" + entry.getValue().getUsername());
            System.out.println("clave=" + entry.getKey() + ", nickanme=" + entry.getValue().toString());
        }
    }

    public void goVerInfoGrupo(View _view) {
        Intent intent = new Intent(MainActivity.this, VerInfoGrupoActivity.class);
        Bundle b = new Bundle();
        System.out.println("CM Antes de passar -------------------------------------------------------- " + cm);
        b.putBoolean("CM", cm); //bool de si es CM o no
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goVerInfoEvento(View _view) {
        startActivity(new Intent(MainActivity.this, VerInfoEventoActivity.class));
    }

    public void goBuscar(View _view) {
        startActivity(new Intent(MainActivity.this, BuscarActivity.class));
    }

    public void goCrearEvento(View _view) {
        startActivity(new Intent(MainActivity.this, CrearEventoActivity.class));
    }

    public void goEditarEvento(View _view) {
        startActivity(new Intent(MainActivity.this, EditarEventoActivity.class));
    }

    public void goLogin(View _view) {
        if (getAuth().getCurrentUser() != null)
            Toast.makeText(MainActivity.this, getString(R.string.ERROR_USUARIO_YA_LOGUEADO), Toast.LENGTH_SHORT).show();
        else
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

    public void goOtroUsuario(View _view) {
        startActivity(new Intent(MainActivity.this, VerInfoOtroUsuarioActivity.class));
    }

    public void showUsuarioActual(View _view) {
        if (getUsuarioActual() != null)
            Toast.makeText(MainActivity.this, (CharSequence) getUsuarioActual().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, getString(R.string.ERROR_NO_LOGUEADO), Toast.LENGTH_SHORT).show();

    }

    public void goCrearUsuario(View _view) {
        startActivity(new Intent(MainActivity.this, CrearUsuarioActivity.class));
    }

    public void goVerEventosUsuariosQueSigo(View _view) {
        startActivity(new Intent(MainActivity.this, VerEventosUsuariosQueSigoActivity.class));
    }

    public void goVerGruposCreados(View _view) {
        startActivity(new Intent(MainActivity.this, VerGruposCreadosActivity.class));
    }

    public void cambiarModo(View _view) {
        if (cm) {
            cm = false;
            Toast.makeText(this, getString(R.string.MODO_USUARIO), Toast.LENGTH_SHORT).show();
            System.out.println("CM Despues del canvio" + cm);
        } else {
            cm = true;
            Toast.makeText(this, getString(R.string.MODO_CM), Toast.LENGTH_SHORT).show();
            System.out.println("CM Despues del canvio" + cm);
        }
    }

    public void goNavBar(View _view) {
        System.out.println("Going navigation Bar!");
        startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
    }

    public void goSignOut(View _view) {
        signOut();
    }

    public void goCrearGrupo(View _view) {
        startActivity(new Intent(MainActivity.this, CrearGrupoActivity.class));
    }

    public void goEditarGrupo(View _view) {
        startActivity(new Intent(MainActivity.this, EditarGrupoActivity.class));
    }

    public void goBuscarEvento(View _view) {
        startActivity(new Intent(MainActivity.this, BuscarEventoActivity.class));
    }

    public void goIndicarTags(View _view) {
        //extra per simular el cas CM
        startActivity(new Intent(MainActivity.this, IndicarTagsActivity.class).putExtra("idGrupo", "grp0-1480690194851"));
    }

    public void goVerMiPerfil(View _view) {
        if (getUsuarioActual() != null)
            startActivity(new Intent(MainActivity.this, PerfilUsuarioActivity.class));
        else
            Toast.makeText(MainActivity.this, getString(R.string.ERROR_NO_LOGUEADO), Toast.LENGTH_SHORT).show();
    }
}
