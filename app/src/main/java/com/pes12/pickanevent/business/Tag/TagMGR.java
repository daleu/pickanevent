package com.pes12.pickanevent.business.Tag;

import android.app.Activity;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefTags;

    public void inicializarDatabase(FirebaseDatabase database) {
        this.database = database;
        bdRefTags = database.getReference("tags");
        bdRefTags.keepSynced(true);
    }

    public String crear(TagEntity _entity)
    {
        bdRefTags.orderByChild("nombreTag").equalTo(_entity.getNombreTag()).addListenerForSingleValueEvent(new ValueEventListener() {
            TagEntity ent;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    System.out.println("YA EXISTE UN TAG CON ESE NOMBRE");
                } else {
                    DatabaseReference grupo = bdRefTags.push();
                    grupo.setValue(ent);
                    grupo.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError arg0) {
            }

            public ValueEventListener setEntity (TagEntity _ent)
            {
                ent=_ent;
                return this;
            }
        }.setEntity(_entity));


        return "";
    }

    public void getInfoTag(Activity _activity, Map<String, Boolean> _idS) {
        bdRefTags.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<String> info = new ArrayList();
            VerInfoGrupoActivity activity;
            Map<String, Boolean> idS;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot tag : dataSnapshot.getChildren()) {
                    TagEntity t = tag.getValue(TagEntity.class);
                    if (idS.containsKey(tag.getKey())) {
                        info.add(t.getNombreTag());
                    }
                }
                activity.mostrarTags(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("EEEERROOOOR");
            }
            public ValueEventListener setActivity (Activity _activity, Map<String, Boolean> _idS)
            {
                activity=(VerInfoGrupoActivity) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }
}
