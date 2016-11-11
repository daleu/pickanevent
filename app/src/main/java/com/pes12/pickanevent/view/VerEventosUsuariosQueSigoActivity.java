package com.pes12.pickanevent.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.pes12.pickanevent.R;
import com.pes12.pickanevent.business.AdapterLista;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VerEventosUsuariosQueSigoActivity extends BaseActivity {

    ListView eventos;

    UsuarioMGR uMGR;
    EventoMGR eMGR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_eventos_usuarios_que_sigo);

        uMGR = MGRFactory.getInstance().getUsuarioMGR();
        eMGR = MGRFactory.getInstance().getEventoMGR();

        ArrayList<Info> info = new ArrayList<>();
        Info i1 = new Info(null, "a", "b", "assistir!");
        Info i2 = new Info(null, "c", "d", "assistir!");
        Info i3 = new Info(null, "a", "b", "assistir!");
        Info i4 = new Info(null, "c", "d", "assistir!");
        Info i5 = new Info(null, "a", "b", "assistir!");
        Info i6 = new Info(null, "c", "d", "assistir!");
        Info i7 = new Info(null, "a", "b", "assistir!");
        Info i8 = new Info(null, "c", "d", "assistir!");
        Info i9 = new Info(null, "a", "b", "assistir!");
        Info i10 = new Info(null, "c", "d", "assistir!");
        Info i11 = new Info(null, "a", "b", "assistir!");
        Info i12 = new Info(null, "c", "d", "assistir!");
        info.add(i1);
        info.add(i2);
        info.add(i3);
        info.add(i4);
        info.add(i5);
        info.add(i6);
        info.add(i7);
        info.add(i8);
        info.add(i9);
        info.add(i10);
        info.add(i11);
        info.add(i12);

        String idUsuario = "a";
        showProgressDialog();

        uMGR.getInfoUsuario(this, idUsuario); //obtengo la info del usuario en la funcion mostrarInfoUsuario


        eventos = (ListView)findViewById(R.id.eventos);


    }

    public void mostrarInfoUsuario(UsuarioEntity _usuario) {
        Map<String, Boolean> usuariosSeguidos = new LinkedHashMap<String, Boolean>();
        //obtengo el username y los eventos que siguen los usuarios a los que sigo en la funcion info Usuarios
        uMGR.getUsers(this, usuariosSeguidos);

    }

    //el string sera el username y el map los eventos que sigue ese usuario
    public void infoUsuarios(Map<String,Map<String,Boolean>> _info) {
        //debo "eliminar" los eventos repetidos y crear una lista nueva en la que habra
        // el evento (String) y todos los usuarios que asistiran(List<String>)
        Map<String, List<String>> usuariosPorEvento = new HashMap<>();
        for (Map.Entry<String, Map<String,Boolean>> eventosUsu: _info.entrySet()) {
            ArrayList<String> usernames = new ArrayList<>();
            usernames.add(eventosUsu.getKey());

            Map<String,Boolean> evento = eventosUsu.getValue();

            for (Map.Entry<String, Map<String,Boolean>> eventosUsuAux: _info.entrySet()) {
                if (!eventosUsu.getKey().equals(eventosUsuAux.getKey()) && eventosUsuAux.getValue().equals(evento)) {
                    usernames.add(eventosUsuAux.getKey());
                }
            }
            _info.remove(evento); //elimino todas las apariciones del evento

            usuariosPorEvento.put(eventosUsu.getKey(), usernames);
        }
        eMGR.getInfoEventosUsuarios(this, usuariosPorEvento);
        hideProgressDialog();
    }

    public void mostrarInfoEventosUsuariosSeguidos(ArrayList<Info> _info) {
        AdapterLista ale = new AdapterLista(VerEventosUsuariosQueSigoActivity.this,R.layout.vista_adapter_lista, _info);
        eventos.setAdapter(ale);
    }


}
