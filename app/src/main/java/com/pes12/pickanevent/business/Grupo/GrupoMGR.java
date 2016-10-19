package com.pes12.pickanevent.business.Grupo;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.persistence.FirebaseSingleton;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.view.VerInfoGrupo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoMGR {

    private final FirebaseDatabase database;
    private DatabaseReference bdRefGrupos;
    private static GrupoMGR singleton;


    public static GrupoMGR getInstance()
    {
        if(singleton==null)
        {
            singleton= new GrupoMGR();
            return singleton;
        }
        else return singleton;
    }

    public GrupoMGR () {

        database = FirebaseSingleton.getInstance();
        bdRefGrupos = database.getReference("grupos");

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

    private void actualizar(String key, GrupoEntity _entity) {
        DatabaseReference grupo = bdRefGrupos.child(key); //recogemos la rama con la ID del grupo en concreto
        grupo.setValue(_entity);
    }

    public String crear(GrupoEntity _entity)
    {
        bdRefGrupos.orderByChild("nombreGrupo").equalTo(_entity.getNombreGrupo()).addListenerForSingleValueEvent(new ValueEventListener() {
            GrupoEntity ent;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println("YA EXISTE UN GRUPO CON ESE NOMBRE");
                } else {
                    DatabaseReference usuario = bdRefGrupos.push();
                    usuario.setValue(ent);
                    usuario.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity (GrupoEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));


        return "";
    }

    public void getInfoGrupo(Activity _activity) {

        bdRefGrupos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,GrupoEntity> map = new LinkedHashMap<String,GrupoEntity>();
            VerInfoGrupo activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot grupo : dataSnapshot.getChildren()) {
                    map.put(grupo.getKey(), grupo.getValue(GrupoEntity.class));
                }
                activity.mostrarInfoGrupo(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(VerInfoGrupo) _activity;
                return this;
            }
        }.setActivity(_activity));

    }
}
