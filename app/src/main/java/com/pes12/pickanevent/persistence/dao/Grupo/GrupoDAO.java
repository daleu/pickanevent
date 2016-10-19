package com.pes12.pickanevent.persistence.dao.Grupo;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoDAO {


    private final FirebaseDatabase database;
    private DatabaseReference bdRefGrupos;
    private LinkedHashMap<String, GrupoEntity> mapGrupos;
    private Activity activity;

    public GrupoDAO (Activity _activity)
    {

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        bdRefGrupos = database.getReference("grupos");
        mapGrupos = new LinkedHashMap<>();
        activity=_activity;
        //initListGrupos(); //inicializa mapGrupos

    }


    public void guardar(String key, GrupoEntity _entity) {
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
                    System.out.println("YA EXISTE UN USER CON ESE USERNAME");
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



    //devuelve un map con todos los grupos (id y UsuarioEntity)
    public Map<String,GrupoEntity> get() {

        return mapGrupos;
    }


    /*private void initListGrupos(){

        bdRefGrupos.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    añadirGrupoMap(usuario.getKey(), usuario.getValue(GrupoEntity.class));

                }
                ((MainActivity)activity).printNicknames(mapGrupos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }
        });

    }*/

    private void añadirGrupoMap(String id, GrupoEntity grupo) {
        mapGrupos.put(id, grupo);
    }
}
