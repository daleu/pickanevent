package com.pes12.pickanevent.business.Usuario;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.EncodeUtil;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.persistence.entity.Usuario.UsuarioEntity;
import com.pes12.pickanevent.view.BaseActivity;
import com.pes12.pickanevent.view.BuscarActivity;
import com.pes12.pickanevent.view.LoginActivity;
import com.pes12.pickanevent.view.MainActivity;
import com.pes12.pickanevent.view.NavigationDrawer;
import com.pes12.pickanevent.view.TimelineFragment;
import com.pes12.pickanevent.view.VerEventosUsuariosQueSigoActivity;
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

public class UsuarioMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefUsuarios;
    //private static UsuarioMGR singleton;


    /*public static UsuarioMGR getInstance()
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
        database = FirebaseFactory.getInstance();
        bdRefUsuarios = database.getReference("usuarios");
    }*/

    public void inicializarDatabase(FirebaseDatabase _database) {
        this.database = _database;
        bdRefUsuarios = _database.getReference(Constantes.BBDD_TABLA_USUARIOS);
        bdRefUsuarios.keepSynced(true);
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
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot usuario : _dataSnapshot.getChildren()) {
                    map.put(usuario.getKey(), usuario.getValue(UsuarioEntity.class));

                }
                activity.printNicknames(map);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(MainActivity)_activity;
                return this;
            }
        }.setActivity(_activity));

    }




    public void actualizar(String _key, UsuarioEntity _entity)
    {


        DatabaseReference usuario = bdRefUsuarios.child(_key); //recogemos la rama con la ID del usuario en concreto

        usuario.setValue(_entity);

    }

    public String crear(UsuarioEntity _entity)
    {

        bdRefUsuarios.orderByChild(Constantes.BBDD_ATRIBUTO_NOMBRE_USUARIO).equalTo(_entity.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity ent;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_USUARIO);
                } else {
                    DatabaseReference usuario = bdRefUsuarios.push();
                    usuario.setValue(ent);
                    usuario.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setEntity (UsuarioEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));


        return "";
    }


    public void getUsersByNickname(Activity _activity, String _text)
    {

        Query queryRef = bdRefUsuarios.orderByChild(UsuarioEntity.ATTRIBUTES.NICKNAME.getValue()).startAt(_text).endAt(_text+"\uf8ff");

        queryRef.addValueEventListener(new ValueEventListener() {
            BuscarActivity activity;
            Map<String,UsuarioEntity> map = new LinkedHashMap<String,UsuarioEntity>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Info> n = new ArrayList<Info>();
                for (DataSnapshot usuario : snapshot.getChildren()) {
                    System.out.println(usuario.getKey());
                   // map.put(usuario.getKey(), usuario.getValue(UsuarioEntity.class));
                    n.add(new Info(null,usuario.getKey(),usuario.getValue(UsuarioEntity.class).getNickname(), "seguir!"));

                }
                activity.printNicknames(n);
                activity.hideProgressDialog();
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



    public void getInfoUsuarioGrupos(Activity _activity, String _id) {
        bdRefUsuarios.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            VerGruposCreadosActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                u = _dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.mostrarInfoUsuarioGrupos(u);
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

    public void getInfoUsuario(Activity _activity, String _id) {
        bdRefUsuarios.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            VerInfoOtroUsuarioActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                u = _dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.mostrarInfoUsuario(u);
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

    public void getInfoUsuarioActivityVerEventosUsuarios(Activity _activity, String _id) {
        bdRefUsuarios.child(_id).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            VerEventosUsuariosQueSigoActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                u = _dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.mostrarInfoUsuario(u);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerEventosUsuariosQueSigoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getUsuarioLogin(BaseActivity _activity, String _uid) {
        bdRefUsuarios.child(_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            BaseActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                u = _dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.setUsuarioActual(u);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (BaseActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getUsers(Activity _activity, Map<String, Boolean> _idU) {
        bdRefUsuarios.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,Map<String,Boolean>> info = new LinkedHashMap<String, Map<String, Boolean>>();
            VerEventosUsuariosQueSigoActivity activity;
            Map<String, Boolean> idU;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot usuario : dataSnapshot.getChildren()) {
                    UsuarioEntity u = usuario.getValue(UsuarioEntity.class);
                    if (idU.containsKey(usuario.getKey())) {
                        info.put(u.getNickname(), u.getIdEventos());
                    }
                }
                activity.infoUsuarios(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity, Map<String, Boolean> _idU)
            {
                activity=(VerEventosUsuariosQueSigoActivity) _activity;
                idU = _idU;
                return this;
            }
        }.setActivity(_activity, _idU));
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void getEventsFromUserFromFragment(Fragment _activity, String _idUsuario) {
        bdRefUsuarios.child(_idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            TimelineFragment activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                u = _dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.getUsuarioEvents(u);
            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Fragment _activity) {
                activity = (TimelineFragment) _activity;
                return this;
            }
        }.setActivity(_activity));
    }
}
