package com.pes12.pickanevent.persistence.dao.Usuario;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioDAO {

    private final FirebaseDatabase database;
    private DatabaseReference bdRefUsuarios;
    private HashMap<String, UsuarioEntity> mapUsuarios;


    public UsuarioDAO ()
    {
        System.out.println("inicialitzoooo");

        database = FirebaseDatabase.getInstance();
        bdRefUsuarios = database.getReference("users");
        mapUsuarios = new HashMap<>();
        childListener(); //inicializa mapUsuarios
    }


    public void guardar(String key, UsuarioEntity _entity)
    {
        DatabaseReference usuariosRef = database.getReference("usuarios"); //recogemos la referencia a la rama de usuarios


        DatabaseReference usuario = usuariosRef.child(key); //recogemos la rama con la ID del usuario en concreto

        usuario.setValue(_entity);

    }

    public String crear(UsuarioEntity _entity)
    {
        DatabaseReference usuarios = database.getReference("usuarios"); //recogemos la referencia a la rama de usuarios
        DatabaseReference usuario = usuarios.push();
        usuario.setValue(_entity);

        return usuario.getKey();
    }


    //devuelve un map con todos los usuarios (id y UsuarioEntity)
    public Map<String,UsuarioEntity> get() {
        System.out.println("CUANDO RETORNO DEL GET MIDE: " + mapUsuarios.size());
        return mapUsuarios;
    }

    private void childListener(){
        System.out.println("ENTRO A CHILD LISTENER 1");

        Log.e("hello! here I am", "ugwhwehovewuivhewouvbervioenveruivbwechi helooooooooow");

        bdRefUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("ENTRO A CHILD LISTENER 2");

                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    //getValue(Class<T> valueType)
                    //This method is used to marshall the data contained in this snapshot
                    // into a class of your choosing.
                    //https://firebase.google.com/docs/reference/android/com/google/firebase/database/DataSnapshot
                    añadirUsuarioMap(usuario.getKey(), usuario.getValue(UsuarioEntity.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void añadirUsuarioMap(String id, UsuarioEntity usuario) {
        mapUsuarios.put(id, usuario);
    }


}
