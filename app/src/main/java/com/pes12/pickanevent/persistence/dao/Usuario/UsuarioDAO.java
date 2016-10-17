package com.pes12.pickanevent.persistence.dao.Usuario;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioDAO {

    private final FirebaseDatabase database;


    public UsuarioDAO ()
    {
        database = FirebaseDatabase.getInstance();

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



    public List<UsuarioEntity> get()
    {
        List<UsuarioEntity> list = new ArrayList<UsuarioEntity>();

        DatabaseReference usuariosRef = database.getReference("usuario"); //recogemos la referencia a la rama de usuarios



        return list;
    }


}
