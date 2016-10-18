package com.pes12.pickanevent.persistence.dao.Usuario;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.pes12.pickanevent.view.MainActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioDAO {

    private final FirebaseDatabase database;
    private DatabaseReference bdRefUsuarios;
    private LinkedHashMap<String, UsuarioEntity> mapUsuarios;
    private Activity activity;

    public UsuarioDAO (Activity _activity)
    {
        System.out.println("inicialitzoooo");

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        bdRefUsuarios = database.getReference("usuarios");
        mapUsuarios = new LinkedHashMap<>();
        activity=_activity;
        initListMainActivity(); //inicializa mapUsuarios

    }


    public void guardar(String key, UsuarioEntity _entity)
    {


        DatabaseReference usuario = bdRefUsuarios.child(key); //recogemos la rama con la ID del usuario en concreto

        usuario.setValue(_entity);


    }

    public String crear(UsuarioEntity _entity)
    {
        bdRefUsuarios.orderByChild("username").equalTo(_entity.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity ent;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println("YA EXISTE UN USER CON ESE USERNAME");
                } else {
                    DatabaseReference usuario = bdRefUsuarios.push();
                    usuario.setValue(ent);
                    usuario.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity (UsuarioEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));


        return "";
    }



    //devuelve un map con todos los usuarios (id y UsuarioEntity)
    public Map<String,UsuarioEntity> get() {

        System.out.println("CUANDO RETORNO DEL GET MIDE: " + mapUsuarios.size());
        return mapUsuarios;
    }

    private void initListMainActivity(){

        bdRefUsuarios.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    añadirUsuarioMap(usuario.getKey(), usuario.getValue(UsuarioEntity.class));

                }
                ((MainActivity)activity).printNicknames(mapUsuarios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }
        });

    }

    private void añadirUsuarioMap(String id, UsuarioEntity usuario) {
        mapUsuarios.put(id, usuario);
    }


}
