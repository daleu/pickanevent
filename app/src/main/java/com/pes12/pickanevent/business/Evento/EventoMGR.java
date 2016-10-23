package com.pes12.pickanevent.business.Evento;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.view.VerInfoEventoActivity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aleue on 21/10/2016.
 */

public class EventoMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefEventos;
    //private static EventoMGR singleton;

    /*public static EventoMGR getInstance()
    {
        if(singleton==null)
        {
            singleton= new EventoMGR();
            return singleton;
        }
        else return singleton;
    }

    public EventoMGR () {

        database = FirebaseFactory.getInstance();
        bdRefEventos = database.getReference("eventos");

    }*/

    public void inicializarDatabase(FirebaseDatabase database) {
        this.database = database;
        bdRefEventos = database.getReference("eventos");
    }

    public String crear(EventoEntity _entity)
    {
        bdRefEventos.orderByChild("nombreEvento").equalTo(_entity.getTitulo()).addListenerForSingleValueEvent(new ValueEventListener() {
            EventoEntity ent;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println("YA EXISTE UN GRUPO CON ESE NOMBRE");
                } else {
                    DatabaseReference evento = bdRefEventos.push();
                    evento.setValue(ent);
                    evento.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity (EventoEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));
        return "";
    }

    private void actualizar(String key, EventoEntity _entity)
    {


        DatabaseReference evento = bdRefEventos.child(key); //recogemos la rama con la ID del evento en concreto

        evento.setValue(_entity);

    }

    public void getInfoGrupo(Activity _activity) {

        bdRefEventos.orderByKey().addValueEventListener(new ValueEventListener() {
            Map<String,EventoEntity> map = new LinkedHashMap<String,EventoEntity>();
            VerInfoEventoActivity activity;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot evento : dataSnapshot.getChildren()) {
                    map.put(evento.getKey(), evento.getValue(EventoEntity.class));
                }
                activity.mostrarInfoEvento(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(VerInfoEventoActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }
}
