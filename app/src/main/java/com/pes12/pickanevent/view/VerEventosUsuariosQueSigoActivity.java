package com.pes12.pickanevent.view;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerEventosUsuariosQueSigoActivity extends BaseActivity {

    ListView eventos;

    UsuarioMGR uMGR;
    EventoMGR eMGR;
    private Map<String, String> eventosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ver_eventos_usuarios_que_sigo);

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        eMGR = MGRFactory.getInstance().getEventoMGR();
        eventos = (ListView) findViewById(R.id.eventos);

        String idUsuario = getAuth().getCurrentUser().getUid();
        showProgressDialog();

        uMGR.getInfoUsuarioActivityVerEventosUsuarios(this, idUsuario); //obtengo la info del usuario en la funcion mostrarInfoUsuario

    }

    public void mostrarInfoUsuario(UsuarioEntity _usuario) {
        //System.out.println("L'usuari " + _usuario.getNickname() + " te " + _usuario.getIdUsuarios().size() + " usuaris que segueix");
        //obtengo el username y los eventos que siguen los usuarios a los que sigo en la funcion info Usuarios
        if (_usuario.getIdEventos() != null) eventosUsuario = _usuario.getIdEventos();
        if (_usuario.getIdUsuarios() != null) uMGR.getUsers(this, _usuario.getIdUsuarios());
        else {
            hideProgressDialog();
            Toast.makeText(VerEventosUsuariosQueSigoActivity.this, "No tienes amigos :D", Toast.LENGTH_SHORT).show();
        }

    }

    //el string sera el username y el map los eventos que sigue ese usuario
    public void infoUsuarios(Map<String, Map<String, String>> _info) {
        System.out.println("Arribo la informacio de " + _info.size() + " usuaris");

        //debo "eliminar" los eventos repetidos y crear una lista nueva en la que habra
        // el evento (String) y todos los usuarios que asistiran(List<String>)
        Map<String, List<String>> usuariosPorEvento = new HashMap<>();
        //loop por los usuarios
        Map<String, String> eventosTratados = eventosUsuario;
        for (Map.Entry<String, Map<String, String>> infoUsu : _info.entrySet()) {
            System.out.println("L'usuari " + infoUsu.getKey() + " te " + infoUsu.getValue().size() + " event/s");
            ArrayList<String> usernames = new ArrayList<>();

            //loop por los eventos del usuario del bucle externo
            for (Map.Entry<String, String> eventoUsu : infoUsu.getValue().entrySet()) {
                //mirare que eventos de otros usuarios coinciden con el mio
                //antes de empezar la iteracion compruebo si ya he tratado ese evento
                //al final del bucle guardare todos los usuarios que asisten a ese evento

                if (!eventosTratados.containsKey(eventoUsu.getKey())) {
                    //obtengo los usuarnames de los usuarios que asisten al evento
                    usernames = usuariosQueAssistiran(_info, eventoUsu);
                    //añado al resultado el evento y los usernames de los usuarios que asisten
                    usuariosPorEvento.put(eventoUsu.getKey(), usernames);
                    System.out.println("al evento: " + eventoUsu.getKey() + " asiste: ");
                    for (int i = 0; i < usernames.size(); ++i) {
                        System.out.println("L'usuari: " + usernames.get(i));
                    }
                }
                eventosTratados.put(eventoUsu.getKey(), eventoUsu.getValue());
            }
        }
        eMGR.getInfoEventosUsuarios(this, usuariosPorEvento);
    }

    private ArrayList<String> usuariosQueAssistiran(Map<String, Map<String, String>> _info, Map.Entry<String, String> _eventoUsu) {
        ArrayList<String> usernames = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> infoUsu : _info.entrySet()) {
            for (Map.Entry<String, String> eventoUsu : infoUsu.getValue().entrySet()) {
                if (eventoUsu.getKey().equals(_eventoUsu.getKey())) {
                    usernames.add(infoUsu.getKey());
                }
            }
        }
        //como minimo retornara el username de usuario del bucle exterior de la funcion infoUsuarios
        return usernames;
    }

    public void mostrarInfoEventosUsuariosSeguidos(ArrayList<Info> _info) {
        System.out.println("tercera funcio: ");
        System.out.println(_info.size());
        for (int i = 0; i < _info.size(); ++i) {
            System.out.println("L'event: " + _info.get(i).primeraLinea);
            System.out.println("L'event: " + _info.get(i).segonaLinea);
        }
        AdapterLista ale = new AdapterLista(VerEventosUsuariosQueSigoActivity.this, R.layout.vista_adapter_lista, _info);
        eventos.setAdapter(ale);

        hideProgressDialog();

    }
}
