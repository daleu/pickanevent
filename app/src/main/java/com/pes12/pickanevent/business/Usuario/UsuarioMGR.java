package com.pes12.pickanevent.business.Usuario;

import android.app.Activity;
import android.text.Editable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.persistence.FirebaseSingleton;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.pes12.pickanevent.view.Buscar;
import com.pes12.pickanevent.view.MainActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioMGR {

    private final FirebaseDatabase database;
    private DatabaseReference bdRefUsuarios;
    private static UsuarioMGR singleton;


    public static UsuarioMGR getInstance()
    {
       if(singleton==null)
       {
           singleton= new UsuarioMGR();
           return singleton;
       }
       else return singleton;
    }

    private UsuarioMGR()
    {
        //database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        database = FirebaseSingleton.getInstance();
        bdRefUsuarios = database.getReference("usuarios");
    }


    public Map<String,UsuarioEntity>  guardarUsuario(Map<String,UsuarioEntity> _entities)
    {
        Map<String,UsuarioEntity> result = new HashMap<String,UsuarioEntity>();
        for (Map.Entry<String, UsuarioEntity> entry : _entities.entrySet()) {

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
    public void getAllUsers(Activity _activity)
    {
        bdRefUsuarios.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,UsuarioEntity> map = new LinkedHashMap<String,UsuarioEntity>();
            MainActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    map.put(usuario.getKey(), usuario.getValue(UsuarioEntity.class));

                }
                activity.printNicknames(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }

            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(MainActivity)_activity;
                return this;
            }
        }.setActivity(_activity));

    }




    private void actualizar(String key, UsuarioEntity _entity)
    {


        DatabaseReference usuario = bdRefUsuarios.child(key); //recogemos la rama con la ID del usuario en concreto

        usuario.setValue(_entity);

    }

    private String crear(UsuarioEntity _entity)
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


    public void getUsersByUsername(Activity _activity, String text)
    {
        Query queryRef = bdRefUsuarios.orderByChild("username").startAt(text).endAt(text+"\uf8ff");

        queryRef.addValueEventListener(new ValueEventListener() {
            Buscar activity;
            Map<String,UsuarioEntity> map = new LinkedHashMap<String,UsuarioEntity>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot usuario : snapshot.getChildren()) {
                    System.out.println(usuario.getKey());
                    map.put(usuario.getKey(), usuario.getValue(UsuarioEntity.class));

                }
                activity.printNicknames(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(Buscar) _activity;
                return this;
            }

        }.setActivity(_activity));
    }
}
