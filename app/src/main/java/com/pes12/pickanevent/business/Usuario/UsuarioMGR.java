package com.pes12.pickanevent.business.Usuario;

import android.app.Activity;
import android.util.Log;

import com.pes12.pickanevent.persistence.dao.Usuario.UsuarioDAO;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioMGR {

    private final UsuarioDAO dao;

    public UsuarioMGR(Activity _activity)
    {
       dao = new UsuarioDAO(_activity);

    }

    public Map<String,UsuarioEntity>  guardar(Map<String,UsuarioEntity> _entities)
    {
        Map<String,UsuarioEntity> result = new HashMap<String,UsuarioEntity>();
        for (Map.Entry<String, UsuarioEntity> entry : _entities.entrySet()) {

            Log.e(entry.getKey(),entry.getValue().getNickname());
           if(entry.getKey()=="")
           {
               result.put(dao.crear(entry.getValue()),entry.getValue());
           }
           else
           {
               dao.guardar(entry.getKey(),entry.getValue());
               result.put(entry.getKey(),entry.getValue());
           }

        }
        return result;
    }

    public Map<String,UsuarioEntity> get()
    {
        return dao.get();
    }




}
