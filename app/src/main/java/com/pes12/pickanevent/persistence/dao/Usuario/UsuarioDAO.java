package com.pes12.pickanevent.persistence.dao.Usuario;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioDAO {

    private final FirebaseDatabase database;


    public UsuarioDAO ()
    {
        database = FirebaseDatabase.getInstance();

    }


    public void guardar(UsuarioEntity _entity)
    {
        DatabaseReference usuariosRef = database.getReference("usuario"); //recogemos la referencia a la rama de usuarios


        DatabaseReference usuario = usuariosRef.child(_entity.getId().toString()); //recogemos la rama con la ID del usuario en concreto


        usuario.child(UsuarioEntity.ATTRIBUTES.USERNAME.getValue()).setValue(_entity.getUsername()); //insertamos los valores en la key (child) del enumerable, así no nos equivocaremos al escribir
        usuario.child(UsuarioEntity.ATTRIBUTES.PASSWORD.getValue()).setValue(_entity.getPassword());
        usuario.child(UsuarioEntity.ATTRIBUTES.EMAIL.getValue()).setValue(_entity.getEmail());
        usuario.child(UsuarioEntity.ATTRIBUTES.CM.getValue()).setValue(_entity.getCm());
        usuario.child(UsuarioEntity.ATTRIBUTES.NICKNAME.getValue()).setValue(_entity.getNickname());

    }
}
