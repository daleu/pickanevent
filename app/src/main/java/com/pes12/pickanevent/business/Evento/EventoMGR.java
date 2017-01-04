package com.pes12.pickanevent.business.Evento;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.view.BuscarEventoActivity;
import com.pes12.pickanevent.view.CrearEventoActivity;
import com.pes12.pickanevent.view.EditarEventoActivity;
import com.pes12.pickanevent.view.EventsFragment;
import com.pes12.pickanevent.view.TimelineFragment;
import com.pes12.pickanevent.view.VerEventosUsuariosQueSigoActivity;
import com.pes12.pickanevent.view.VerInfoEventoActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;
import com.pes12.pickanevent.view.VerInfoOtroUsuarioActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aleue on 21/10/2016.
 */

public class EventoMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefEventos;

    public void inicializarDatabase(FirebaseDatabase _database) {
        this.database = _database;
        bdRefEventos = _database.getReference(EventoEntity.NOMBRETABLA);
        bdRefEventos.keepSynced(true);
    }

    public String crear(EventoEntity _entity) {
        bdRefEventos.orderByChild(EventoEntity.ATTRIBUTES.TITULO.getValue()).equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
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

            public ValueEventListener setEntity(EventoEntity _ent) {
                ent = _ent;
                return this;
            }
        }.setEntity(_entity));
        return "";
    }

    public String crearConRedireccion(Activity _activity, EventoEntity _entity) {
        bdRefEventos.orderByChild(EventoEntity.ATTRIBUTES.TITULO.getValue()).equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            CrearEventoActivity activity;
            String id;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                } else {
                    DatabaseReference evento = bdRefEventos.push();
                    evento.setValue(ent);
                    id = evento.getKey();
                }
                activity.redirecionarConIdEvento(id);
            }

            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setActivity(Activity _activity, EventoEntity _ent) {
                activity = (CrearEventoActivity) _activity;
                ent = _ent;
                return this;
            }
        }.setActivity(_activity, _entity));


        return "";
    }

    public void actualizar(String _key, EventoEntity _entity) {
        DatabaseReference evento = bdRefEventos.child(_key); //recogemos la rama con la ID del evento en concreto

        evento.setValue(_entity);

    }

    public void getInfoEvento(Activity _activity) {

        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String, EventoEntity> map = new LinkedHashMap<String, EventoEntity>();
            EventoEntity e;
            VerInfoEventoActivity activity;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }
                Map<String,EventoEntity> evento = new HashMap<String,EventoEntity>();
                /*e = _dataSnapshot.getValue((EventoEntity.class));*/
                activity.mostrarInfoEvento(evento);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerInfoEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getInfoEventoEditar(Activity _activity) {

        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,EventoEntity> map = new LinkedHashMap<String,EventoEntity>();
            EditarEventoActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot evento : dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }
                activity.mostrarInfoEventoEditar(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR INESPERADO");
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity = (EditarEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getInfoEventoElegido(Activity _activity, String _attr, final String _val) {
        final String aux = _attr;
        final CharSequence aux2 = _val.toLowerCase();
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            BuscarEventoActivity activity;
            ArrayList<Info> n = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    if (aux.equals(EventoEntity.ATTRIBUTES.TITULO.getValue()) && evento.getValue(EventoEntity.class).getTitulo() != null) {
                        if (evento.getValue(EventoEntity.class).getTitulo().toLowerCase().contains(aux2)) {
                            if (evento.getValue(EventoEntity.class).getPrecio() != null) {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        evento.getValue(EventoEntity.class).getPrecio()+"€", "asistir!"));
                            }
                            else {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        "Gratis", "asistir!"));
                            }
                        }
                    }
                    else if (aux.equals("localizacion") && evento.getValue(EventoEntity.class).getLocalizacion() != null) {
                        if (evento.getValue(EventoEntity.class).getLocalizacion().toLowerCase().contains(aux2)) {
                            if (evento.getValue(EventoEntity.class).getPrecio() != null) {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        evento.getValue(EventoEntity.class).getPrecio()+"€", "asistir!"));
                            }
                            else {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        "Gratis", "asistir!"));
                            }
                        }
                    }
                    else if (aux.equals("precio")) {
                        String precio = evento.getValue(EventoEntity.class).getPrecio();
                        Double aux = null;
                        if (precio != null) aux = Double.parseDouble(precio);
                        if (precio == null && _val.equals("0")) {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        "Gratis", "asistir!"));
                        }
                        if (precio != null) {
                            if (precio.equals(_val) && _val.equals("0")) {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        "Gratis", "asistir!"));
                            }
                            if(_val.equals("50") && aux < 50){
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        precio+"€", "asistir!"));
                            }
                            if(_val.equals("50<>200") && 50<=aux && aux<=200) {
                                n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                        precio+"€", "asistir!"));
                            }
                            if (_val.equals(">200")) {
                                if (aux > 200) {
                                    n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                            precio+"€", "asistir!"));
                                }
                            }
                        }
                    }
                    else if (aux.equals("dia") && evento.getValue(EventoEntity.class).getDataInici() != null) {
                        long tiempo = evento.getValue(EventoEntity.class).getDataInici().getTime();
                        if (evento.getValue(EventoEntity.class).getDataFinal() != null) {
                            long tiempoFinal = evento.getValue(EventoEntity.class).getDataFinal().getTime();
                            long auxVal = Long.parseLong(_val) + 86400000;
                            if ((Long.parseLong(_val) <= tiempo && auxVal > tiempoFinal) ||
                                    (tiempo <= Long.parseLong(_val) && Long.parseLong(_val) <= tiempoFinal && tiempoFinal < auxVal) ||
                                    (tiempo >= Long.parseLong(_val) && auxVal < tiempoFinal && tiempo < auxVal)) {
                                if (evento.getValue(EventoEntity.class).getPrecio() != null) {
                                    n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                            evento.getValue(EventoEntity.class).getPrecio()+"€", "asistir!"));
                                }
                                else {
                                    n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                            "Gratis", "asistir!"));
                                }
                            }
                        }
                        else {
                            if (Long.parseLong(_val) == tiempo) {
                                if (evento.getValue(EventoEntity.class).getPrecio() != null) {
                                    n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                            evento.getValue(EventoEntity.class).getPrecio()+"€", "asistir!"));
                                }
                                else {
                                    n.add(new Info(null, evento.getValue(EventoEntity.class).getTitulo(),
                                            "Gratis", "asistir!"));
                                }
                            }
                        }
                    }
                }
                activity.mostrarInfoEventoElegido(n);
                activity.hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (BuscarEventoActivity) _activity;
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

    public void getInfoEventosGrupo(Activity _activity, Map<String, String> _idS, final Boolean cm) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<Info> info = new ArrayList();
            VerInfoGrupoActivity activity;
            String id;
            Map<String, String> idS;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey())) {
                        String textoBoton = "Asistir!";
                        if (cm) textoBoton = "Editar";
                        System.out.println(textoBoton + "" + cm);
                        info.add(new Info(StringToBitMap(e.getImagen()), e.getTitulo(), "horariii", textoBoton));
                    }
                }
                activity.mostrarEventosGrupo(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity, Map<String, String> _idS) {
                activity = (VerInfoGrupoActivity) _activity;
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
                        info.add(new Info(StringToBitMap(e.getImagen()), "Asistiran: " + usuariosPorEvento.get(evento.getKey()).toString(), e.getTitulo(), "No Assistir!"));
                    }
                }

                activity.mostrarInfoEventosUsuariosSeguidos(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity, Map<String, List<String>> _usuariosPorEvento) {
                activity = (VerEventosUsuariosQueSigoActivity) _activity;
                usuariosPorEvento = _usuariosPorEvento;
                return this;
            }
        }.setActivity(_activity, _usuariosPorEvento));
    }

    public void getInfoEventosUsuarioFromFragment(Fragment _activity, Map<String, String> _idS) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
           //ArrayList<Info> info = new ArrayList();
            //ArrayList<EventoEntity> eventos = new ArrayList();
            TimelineFragment activity;
            Map<String, String> idS;
            Map<String,EventoEntity> gUI = new HashMap<String, EventoEntity>();

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                Date actual = new Date();
                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey()) && actual.before(e.getDataInici())) {
                        gUI.put(evento.getKey(),e);
                    }
                }
                activity.mostrarEventosUsuario(gUI);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Fragment _activity, Map<String, String> _idS) {
                activity = (TimelineFragment) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }

    public void getEventosForFragment(Fragment _activity, Map<String, String> _idS) {
        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            EventsFragment activity;
            Map<String, String> idS;
            Map<String,EventoEntity> gUI = new HashMap<String, EventoEntity>();

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey())) {
                        gUI.put(evento.getKey(),e);
                    }
                }
                activity.mostrarEventosUsuario(gUI);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Fragment _activity, Map<String, String> _idS) {
                activity = (EventsFragment) _activity;
                idS = _idS;
                Log.e("vinga va", idS.toString());
                return this;
            }
        }.setActivity(_activity, _idS));
    }
}
