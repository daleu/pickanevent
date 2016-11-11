package com.pes12.pickanevent.business.Usuario;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.pes12.pickanevent.view.VerEventosUsuariosQueSigoActivity;
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

    public void inicializarDatabase(FirebaseDatabase database) {
        this.database = database;
        bdRefUsuarios = database.getReference("usuarios");
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




    public void actualizar(String key, UsuarioEntity _entity)
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
            BuscarActivity activity;
            Map<String,UsuarioEntity> map = new LinkedHashMap<String,UsuarioEntity>();
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Info> n = new ArrayList<Info>();
                for (DataSnapshot usuario : snapshot.getChildren()) {
                    System.out.println(usuario.getKey());
                   // map.put(usuario.getKey(), usuario.getValue(UsuarioEntity.class));
                    n.add(new Info(null,usuario.getKey(),usuario.getValue(UsuarioEntity.class).getUsername(), "seguir!"));

                }
                activity.printNicknames(n);
                activity.hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(BuscarActivity) _activity;
                return this;
            }

        }.setActivity(_activity));
    }

    public void login(Activity _activity, String _user, String _password)
    {
        Query queryRef = bdRefUsuarios.orderByChild("username").equalTo(_user);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            LoginActivity activity;
            String password;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    UsuarioEntity usuario=user.getValue(UsuarioEntity.class);

                    if(usuario!=null && usuario.getPassword()!=null) {
                        if (usuario.getPassword().equals(EncodeUtil.encodePasswordSHA1(password))) {
                            System.out.println("Login correcto");
                            activity.setUsuarioActual(usuario);
                        } else System.out.println("Login INcorrecto");
                    }

                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            public ValueEventListener init (Activity _activity, String _password)
            {
                password = _password;
                activity=(LoginActivity) _activity;
                return this;
            }

        }.init(_activity,_password));

    }

    public void getInfoUsuario(Activity _activity, String id) {
        bdRefUsuarios.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            VerInfoOtroUsuarioActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.mostrarInfoUsuario(u);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (VerInfoOtroUsuarioActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public void getUsuarioLogin(BaseActivity _activity, String uid) {
        bdRefUsuarios.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            UsuarioEntity u;
            BaseActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue((UsuarioEntity.class)); //<------------

                activity.setUsuarioActual(u);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
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
}
