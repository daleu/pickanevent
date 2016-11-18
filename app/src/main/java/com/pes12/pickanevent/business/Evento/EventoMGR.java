package com.pes12.pickanevent.business.Evento;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.view.EditarEventoActivity;
import com.pes12.pickanevent.view.TimelineFragment;
import com.pes12.pickanevent.view.VerEventosUsuariosQueSigoActivity;
import com.pes12.pickanevent.view.VerInfoEventoActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;
import com.pes12.pickanevent.view.VerInfoOtroUsuarioActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aleue on 21/10/2016.
 */

public class EventoMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefEventos;
    //private static EventoMGR singleton;

    /*public static EventoMGR getInstance()
    {
        if(singleton==null)
        {
            singleton= new EventoMGR();
            return singleton;
        }
        else return singleton;
    }

    public EventoMGR () {

        database = FirebaseFactory.getInstance();
        bdRefEventos = database.getReference("eventos");

    }*/

    public void inicializarDatabase(FirebaseDatabase _database) {
        this.database = _database;
        bdRefEventos = _database.getReference(Constantes.BBDD_TABLA_EVENTOS);
        bdRefEventos.keepSynced(true);
    }

    public String crear(EventoEntity _entity)
    {
        bdRefEventos.orderByChild("titulo").equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                } else {
                    DatabaseReference evento = bdRefEventos.push();
                    evento.setValue(ent);
                    evento.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity (EventoEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));
        return "";
    }

    public void actualizar(String _key, EventoEntity _entity)
    {
        DatabaseReference evento = bdRefEventos.child(_key); //recogemos la rama con la ID del evento en concreto

        evento.setValue(_entity);

    }

    public void getInfoEvento(Activity _activity) {

        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,EventoEntity> map = new LinkedHashMap<String,EventoEntity>();
            VerInfoEventoActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot evento : dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }
                activity.mostrarInfoEvento(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(VerInfoEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getInfoEventoEditar(Activity _activity) {

        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,EventoEntity> map = new LinkedHashMap<String,EventoEntity>();
            EditarEventoActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }
                activity.mostrarInfoEvento(map);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(EditarEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getInfoEventoUsuario(Activity _activity, String _id) {
        bdRefEventos.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity e;
            VerInfoOtroUsuarioActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                e = _dataSnapshot.getValue((EventoEntity.class));
                //System.out.println(g.getNickname());
                //System.out.println(g.getImagen());//<------------

                activity.rellenarListaEventos(e);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerInfoOtroUsuarioActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getInfoEventosGrupo(Activity _activity, Map<String, Boolean> _idS, final Boolean cm) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<Info> info = new ArrayList();
            VerInfoGrupoActivity activity;
            String id;
            Map<String, Boolean> idS;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey())) {
                        String textoBoton = "Asistir!";
                        if (cm) textoBoton = "Editar";
                        System.out.println(textoBoton+ ""+ cm);
                        info.add(new Info(StringToBitMap(e.getImagen()), e.getTitulo(), e.getHorario(), textoBoton));
                    }
                }
                activity.mostrarEventosGrupo(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity, Map<String, Boolean> _idS)
            {
                activity=(VerInfoGrupoActivity) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }

    private Bitmap StringToBitMap(String _encodedString) {
        try {
            byte[] encodeByte = Base64.decode(_encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void getInfoEventosUsuarios(Activity _activity, Map<String, List<String>> _usuariosPorEvento) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            VerEventosUsuariosQueSigoActivity activity;
            Map<String, List<String>> usuariosPorEvento;
            ArrayList<Info> info = new ArrayList<Info>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot evento : dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (usuariosPorEvento.containsKey(evento.getKey())) {
                        info.add(new Info(StringToBitMap(e.getImagen()), "Asistiran: "+ usuariosPorEvento.get(evento.getKey()).toString() , e.getTitulo(), "Asistir!"));
                    }
                }

                activity.mostrarInfoEventosUsuariosSeguidos(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity, Map<String, List<String>> _usuariosPorEvento)
            {
                activity=(VerEventosUsuariosQueSigoActivity) _activity;
                usuariosPorEvento = _usuariosPorEvento;
                return this;
            }
        }.setActivity(_activity, _usuariosPorEvento));
    }

    public void getInfoEventosUsuarioFromFragment(Fragment _activity, Map<String, Boolean> _idS) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<Info> info = new ArrayList();
            TimelineFragment activity;
            Map<String, Boolean> idS;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey())) {
                        info.add(new Info(StringToBitMap(e.getImagen()), e.getTitulo(), e.getHorario(), "Asistir!"));
                    }
                }
                activity.mostrarEventosUsuario(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Fragment _activity, Map<String, Boolean> _idS) {
                activity = (TimelineFragment) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }
}
