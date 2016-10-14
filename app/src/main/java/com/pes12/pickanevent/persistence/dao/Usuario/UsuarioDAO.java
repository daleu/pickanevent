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


    public void guardar()
    {
        DatabaseReference usuariosRef = database.getReference("usuario");
        DatabaseReference usuario = usuariosRef.child("1");
        usuario.child("username").setValue("oscar435");
        usuario.child("password").setValue("passsimple");
        usuario.child("email").setValue("oscar@ajshda.com");
        usuario.child("cm").setValue("0");
        usuario.child("nickname").setValue("legault");

    }
}
