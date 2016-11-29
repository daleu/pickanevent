package com.pes12.pickanevent.business.Grupo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.pes12.pickanevent.view.BuscarActivity;
import com.pes12.pickanevent.view.TimelineFragment;
import com.pes12.pickanevent.view.VerGruposCreadosActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;
import com.pes12.pickanevent.view.VerInfoOtroUsuarioActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefGrupos;
    //private static GrupoMGR singleton;


    /*public static GrupoMGR getInstance()
    {
        if(singleton==null)
        {
            singleton= new GrupoMGR();
            return singleton;
        }
        else return singleton;
    }

    public GrupoMGR () {

        database = FirebaseFactory.getInstance();
        bdRefGrupos = database.getReference("grupos");

    }*/

    public void inicializarDatabase(FirebaseDatabase _database) {
        this.database = _database;
        bdRefGrupos = _database.getReference(Constantes.BBDD_TABLA_GRUPOS);
        bdRefGrupos.keepSynced(true);
    }

    public Map<String,GrupoEntity> guardarGrupo(Map<String,GrupoEntity> _entities)
    {
        Map<String,GrupoEntity> result = new HashMap<String,GrupoEntity>();
        for (Map.Entry<String, GrupoEntity> entry : _entities.entrySet()) {

            Log.e(entry.getKey(),entry.getValue().getNickname());
            if(entry.getKey()=="")
            {
                result.put(crear(entry.getValue()),entry.getValue());
            }
            else
            {
                actualizar(entry.getKey(),entry.getValue());
                result.put(entry.getKey(),entry.getValue());
            }

        }

        return result;
    }

    private void actualizar(String _key, GrupoEntity _entity) {
        DatabaseReference grupo = bdRefGrupos.child(_key); //recogemos la rama con la ID del grupo en concreto
        grupo.setValue(_entity);
    }

    public String crear(GrupoEntity _entity)
    {
        bdRefGrupos.orderByChild(GrupoEntity.ATTRIBUTES.NOMBREGRUPO.getValue()).equalTo(_entity.getNombreGrupo()).addListenerForSingleValueEvent(new ValueEventListener() {
            GrupoEntity ent;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_GRUPO);
                } else {
                    DatabaseReference grupo = bdRefGrupos.push();
                    grupo.setValue(ent);
                    grupo.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setEntity (GrupoEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));


        return "";
    }

    public void getInfoGrupo(Activity _activity, String id) {
        bdRefGrupos.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            GrupoEntity g;
            VerInfoGrupoActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                g = _dataSnapshot.getValue((GrupoEntity.class)); //<------------

                activity.mostrarInfoGrupo(g);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerInfoGrupoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));

    }

    public void getInfoGrupoUsuario(Activity _activity, String _id) {
        bdRefGrupos.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            GrupoEntity g;
            VerInfoOtroUsuarioActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                g = _dataSnapshot.getValue((GrupoEntity.class));
                //System.out.println(g.getNickname());
                //System.out.println(g.getImagen());//<------------

                activity.rellenarListaGrupos(g);
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

    public void getInfoGruposCreados(Activity _activity, String _id) {
        bdRefGrupos.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            GrupoEntity g;
            VerGruposCreadosActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                g = _dataSnapshot.getValue((GrupoEntity.class));
                //System.out.println(g.getNickname());
                //System.out.println(g.getImagen());//<------------

                activity.rellenarListaGrupos(g);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerGruposCreadosActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getGruposByNombreGrupo(Activity _activity, String _text)
    {
        Query queryRef = bdRefGrupos.orderByChild(GrupoEntity.ATTRIBUTES.NOMBREGRUPO.getValue()).startAt(_text).endAt(_text+"\uf8ff");

        queryRef.addValueEventListener(new ValueEventListener() {
            BuscarActivity activity;
            Map<String,GrupoEntity> map = new LinkedHashMap<String,GrupoEntity>();
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                ArrayList<Info> n = new ArrayList<Info>();
                for (DataSnapshot grupo : _snapshot.getChildren()) {
                    System.out.println(grupo.getKey());
                    //map.put(grupo.getKey(), grupo.getValue(GrupoEntity.class));
                    n.add(new Info(null,grupo.getKey(),grupo.getValue(GrupoEntity.class).getNombreGrupo(), "seguir!"));

                }
                activity.printNombresGrupo(n);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {

            }

            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(BuscarActivity) _activity;
                return this;
            }

        }.setActivity(_activity));
    }

    public void getGrupoEventosForFragment(Fragment _activity, Map<String, String> _idU) {
        bdRefGrupos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,Map<String,String>> info = new LinkedHashMap<String, Map<String, String>>();
            TimelineFragment activity;
            Map<String, String> idU;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot);
                for (DataSnapshot grupo : dataSnapshot.getChildren()) {
                    GrupoEntity u = grupo.getValue(GrupoEntity.class);
                    if (idU.containsKey(grupo.getKey())) {
                        if(u.getIdEventos()!=null) info.put(u.getNombreGrupo(), u.getIdEventos());
                    }
                }
                activity.getAllGrupoEvents(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Fragment _activity, Map<String, String> _idU)
            {
                activity=(TimelineFragment) _activity;
                idU = _idU;
                return this;
            }
        }.setActivity(_activity, _idU));
    }
}
