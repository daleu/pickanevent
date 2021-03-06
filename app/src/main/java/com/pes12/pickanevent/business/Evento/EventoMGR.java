package com.pes12.pickanevent.business.Evento;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.view.BaseActivity;
import com.pes12.pickanevent.view.BuscarEventoActivity;
import com.pes12.pickanevent.view.CrearEventoActivity;
import com.pes12.pickanevent.view.EditarEventoActivity;
import com.pes12.pickanevent.view.EventsFragment;
import com.pes12.pickanevent.view.TimelineFragment;
import com.pes12.pickanevent.view.VerEventosUsuariosQueSigoActivity;
import com.pes12.pickanevent.view.VerInfoEventoActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;
import com.pes12.pickanevent.view.VerInfoOtroUsuarioActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
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

    public String crear(EventoEntity _entity, InputStream _is, Activity _activity) {
        bdRefEventos.orderByChild(EventoEntity.ATTRIBUTES.TITULO.getValue()).equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            InputStream is;
            CrearEventoActivity activity;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                } else {
                    DatabaseReference evento = bdRefEventos.push();
                    evento.setValue(ent);

                    if (is != null) MGRFactory.getInstance().getImagenEventoMGR().subirImagen(is,ent,evento.getKey(),activity);
                    activity.addEventoAlGrupo(evento.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity(EventoEntity _ent,InputStream _is, Activity _activity) {
                ent = _ent;
                is = _is;
                activity = (CrearEventoActivity) _activity;
                return this;
            }
        }.setEntity(_entity,_is, _activity));
        return "";
    }

    public String crearConRedireccion(Activity _activity, EventoEntity _entity, InputStream _is) {
        bdRefEventos.orderByChild(EventoEntity.ATTRIBUTES.TITULO.getValue()).equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            CrearEventoActivity activity;
            String idEvento;
            InputStream is;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                    activity.errorTituloRepetido();
                } else {
                    DatabaseReference evento = bdRefEventos.push();
                    evento.setValue(ent);
                    idEvento = evento.getKey();
                    activity.getUsuarioActual().getIdEventos().put(idEvento, ent.getTitulo());
                    MGRFactory.getInstance().getUsuarioMGR().actualizar(activity.getUsuarioId(), activity.getUsuarioActual());
                    //MGRFactory.getInstance().getGrupoMGR().addEventoAlGrupo(ent.getIdGrup(), idEvento, ent.getTitulo());
                    if (is != null) MGRFactory.getInstance().getImagenEventoMGR().subirImagen(is,ent,evento.getKey(),activity);
                    else activity.redireccionarConIdEvento(idEvento);
                }

            }

            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setActivity(Activity _activity, EventoEntity _ent, InputStream _is) {
                activity = (CrearEventoActivity) _activity;
                ent = _ent;
                is = _is;
                return this;
            }
        }.setActivity(_activity, _entity, _is));


        return "";
    }

    public void actualizar(String _key, EventoEntity _entity) {
        DatabaseReference evento = bdRefEventos.child(_key); //recogemos la rama con la ID del evento en concreto

        evento.setValue(_entity);

    }

    public String actualizarConRedireccion(String _key, Activity _activity, EventoEntity _entity, InputStream _is) {
        bdRefEventos.orderByChild(EventoEntity.ATTRIBUTES.TITULO.getValue()).equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            EditarEventoActivity activity;
            String id;
            InputStream is;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                    activity.redireccionarConIdEvento(id);
                } else {
                    DatabaseReference evento = bdRefEventos.child(id);
                    evento.setValue(ent);
                    if (is != null) MGRFactory.getInstance().getImagenEventoMGR().subirImagen(is,ent,evento.getKey(),activity);
                    else activity.redireccionarConIdEvento(id);
                }

            }

            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setActivity(String _key, Activity _activity, EventoEntity _ent, InputStream _is) {
                activity = (EditarEventoActivity) _activity;
                id = _key;
                ent = _ent;
                is = _is;
                return this;
            }
        }.setActivity(_key, _activity, _entity, _is));


        return "";
    }

    public void getInfoEvento(Activity _activity, String idEvento) {

        bdRefEventos.child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            Map<String, EventoEntity> map = new LinkedHashMap<String, EventoEntity>();
            EventoEntity e;
            VerInfoEventoActivity activity;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                /*for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }*/
                Map<String,EventoEntity> evento = new HashMap<String,EventoEntity>();
                e = _dataSnapshot.getValue((EventoEntity.class));
                evento.put(_dataSnapshot.getKey(),e);
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

    public void getInfoEventoEditar(Activity _activity, String id) {
        bdRefEventos.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity e;
            EditarEventoActivity activity;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                e = _dataSnapshot.getValue((EventoEntity.class)); //<------------
                activity.mostrarInfoEventoEditar(e);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (EditarEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void borrarEventoEnGrupo(Activity _activity, final String _idEvento) {
        bdRefEventos.child(_idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity g;
            BaseActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                g = _dataSnapshot.getValue((EventoEntity.class));
                MGRFactory.getInstance().getGrupoMGR().borrarEventoMapGrupo(activity, _idEvento, g.getIdGrup());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (BaseActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void borrarEvento(String _key) {
        bdRefEventos.child(_key).removeValue();
    }

    public void getInfoEventoElegido(Activity _activity, String _attr, final String _val) {
        final String aux = _attr;
        final CharSequence aux2 = _val.toLowerCase();
        bdRefEventos.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            BuscarEventoActivity activity;
            ArrayList<Info> n = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot e : _dataSnapshot.getChildren()) {
                    EventoEntity evento = e.getValue(EventoEntity.class);
                    if (aux.equals(EventoEntity.ATTRIBUTES.TITULO.getValue()) && evento.getTitulo() != null) {
                        if (evento.getTitulo().toLowerCase().contains(aux2)) {
                            Info aux = new Info(evento.getImagen(), evento.getTitulo(),
                                    EventDate(evento.getDataInDate(),evento.getDataFiDate()), Constantes.INFO_ASISTIR);
                            aux.setId((String) e.getKey());
                            aux.setTipus(Constantes.INFO_EVENTO);
                            aux.setBotonVisible(false);
                            n.add(aux);
                        }
                    }
                    else if (aux.equals(Constantes.INFO_LOCALIZACION) && evento.getLocalizacion() != null) {
                        if (evento.getLocalizacion().toLowerCase().contains(aux2)) {
                            Info aux = new Info(evento.getImagen(), evento.getTitulo(),
                                    evento.getLocalizacion(), Constantes.INFO_ASISTIR);
                            aux.setId((String) e.getKey());
                            aux.setTipus(Constantes.INFO_EVENTO);
                            aux.setBotonVisible(false);
                            n.add(aux);
                        }
                    }
                    else if (aux.equals(Constantes.INFO_PRECIO)) {
                        String precio = evento.getPrecio();
                        Double aux = null;
                        if (!precio.equals("")) aux = Double.parseDouble(precio);
                        else aux = 0.0;
                        if (aux == 0.0 && _val.equals("0")) {
                            Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                    Constantes.INFO_GRATIS, Constantes.INFO_ASISTIR);
                            auxEvento.setId((String) e.getKey());
                            auxEvento.setTipus(Constantes.INFO_EVENTO);
                            auxEvento.setBotonVisible(false);
                            n.add(auxEvento);
                        }
                        if (aux > 0.0) {
                            if(_val.equals("50") && aux < 50.0){
                                Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                        precio+Constantes.INFO_SYMBOL_EURO, Constantes.INFO_ASISTIR);
                                auxEvento.setId((String) e.getKey());
                                auxEvento.setTipus(Constantes.INFO_EVENTO);
                                auxEvento.setBotonVisible(false);
                                n.add(auxEvento);
                            }
                            if(_val.equals("50<>200") && 50<=aux && aux<=200) {
                                Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                        precio+Constantes.INFO_SYMBOL_EURO, Constantes.INFO_ASISTIR);
                                auxEvento.setId((String) e.getKey());
                                auxEvento.setTipus(Constantes.INFO_EVENTO);
                                auxEvento.setBotonVisible(false);
                                n.add(auxEvento);
                            }
                            if (_val.equals(">200")) {
                                if (aux > 200) {
                                    Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                            precio+Constantes.INFO_SYMBOL_EURO, Constantes.INFO_ASISTIR);
                                    auxEvento.setId((String) e.getKey());
                                    auxEvento.setTipus(Constantes.INFO_EVENTO);
                                    auxEvento.setBotonVisible(false);
                                    n.add(auxEvento);
                                }
                            }
                        }
                    }
                    else if (aux.equals(Constantes.INFO_DIA) && evento.getDataInDate() != null) {
                        long tiempo = evento.getDataInDate().getTime();
                        if (evento.getDataFiDate() != null) {
                            long tiempoFinal = evento.getDataFiDate().getTime();
                            long auxVal = Long.parseLong(_val) + 86400000;
                            if ((Long.parseLong(_val) <= tiempo && auxVal > tiempoFinal) ||
                                (tiempo <= Long.parseLong(_val) && Long.parseLong(_val) <= tiempoFinal && tiempoFinal < auxVal) ||
                                (tiempo >= Long.parseLong(_val) && auxVal < tiempoFinal && tiempo < auxVal)) {

                                Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                        EventDate(evento.getDataInDate(),evento.getDataFiDate()), Constantes.INFO_ASISTIR);
                                auxEvento.setId((String) e.getKey());
                                auxEvento.setTipus(Constantes.INFO_EVENTO);
                                auxEvento.setBotonVisible(false);
                                n.add(auxEvento);
                            }
                        }
                        else {
                            if (Long.parseLong(_val) == tiempo) {
                                    Info auxEvento = new Info(evento.getImagen(), evento.getTitulo(),
                                            EventDate(evento.getDataInDate(),evento.getDataFiDate()), Constantes.INFO_ASISTIR);
                                    auxEvento.setId((String) e.getKey());
                                    auxEvento.setTipus(Constantes.INFO_EVENTO);
                                    auxEvento.setBotonVisible(false);
                                    n.add(auxEvento);
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

    public void getInfoEventoUsuario(Activity _activity, final String _id) {
        bdRefEventos.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity e;
            VerInfoOtroUsuarioActivity activity;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                e = _dataSnapshot.getValue((EventoEntity.class));
                //System.out.println(g.getNickname());
                //System.out.println(g.getImagen());//<------------

                activity.rellenarListaEventos(e, _id);
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
        bdRefEventos.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Info> info = new ArrayList();
            VerInfoGrupoActivity activity;
            String id;
            Map<String, String> idS;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot evento : _dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (idS.containsKey(evento.getKey())) {
                        String textoBoton = Constantes.INFO_ASISTIR_CAMEL;
                        if (cm) textoBoton = Constantes.INFO_EDITAR;
                        Info aux = new Info(e.getImagen(), e.getTitulo(), EventDate(e.getDataInDate(),e.getDataFiDate()), textoBoton);
                        aux.setTipus(Constantes.INFO_EVENTO);
                        aux.setId((String) evento.getKey());
                        aux.setTipus(Constantes.INFO_EVENTO);
                        aux.setBotonVisible(false);
                        info.add(aux);

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


    public void getInfoEventosUsuarios(Activity _activity, Map<String, List<String>> _usuariosPorEvento) {
        bdRefEventos.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            VerEventosUsuariosQueSigoActivity activity;
            Map<String, List<String>> usuariosPorEvento;
            ArrayList<Info> info = new ArrayList<Info>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot evento : dataSnapshot.getChildren()) {
                    EventoEntity e = evento.getValue(EventoEntity.class);
                    if (usuariosPorEvento.containsKey(evento.getKey())) {
                        info.add(new Info(null, "Asistiran: " + usuariosPorEvento.get(evento.getKey()).toString(), e.getTitulo(), Constantes.INFO_NO_ASISTIR));
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
        bdRefEventos.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if (idS.containsKey(evento.getKey()) && actual.before(e.getDataInDate())) {
                        gUI.put(evento.getKey(),e);
                    }
                }
                if (activity.isAdded())
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

    public String EventDate(Date ini, Date fi){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String inici = sdfDate.format(ini);
        String fina = sdfDate.format(fi);
        String data = inici + "h  " + fina + "h";
        return data;
    }

    public void getEventosForFragment(Fragment _activity, Map<String, String> _idS) {
        bdRefEventos.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
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
                if (activity.isAdded())
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
