package com.pes12.pickanevent.business.Tag;

import android.app.Activity;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.InfoTags;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.view.IndicarTagsActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagMGR {

    private FirebaseDatabase database;
    private DatabaseReference bdRefTags;

    public void inicializarDatabase(FirebaseDatabase _database) {
        this.database = _database;
        bdRefTags = _database.getReference(Constantes.BBDD_TABLA_TAGS);
        bdRefTags.keepSynced(true);
    }

    public String crear(TagEntity _entity)
    {
        bdRefTags.orderByChild(Constantes.BBDD_ATRIBUTO_NOMBRE_TAG).equalTo(_entity.getNombreTag()).addListenerForSingleValueEvent(new ValueEventListener() {
            TagEntity ent;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_TAG);
                } else {
                    DatabaseReference grupo = bdRefTags.push();
                    grupo.setValue(ent);
                    grupo.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError _arg0) {
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
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot tag : _dataSnapshot.getChildren()) {
                    TagEntity t = tag.getValue(TagEntity.class);
                    if (idS.containsKey(tag.getKey())) {
                        info.add(t.getNombreTag());
                    }
                }
                activity.mostrarTags(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity, Map<String, Boolean> _idS)
            {
                activity=(VerInfoGrupoActivity) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }

    public void getTodosLosTags(Activity _activity) {
        bdRefTags.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<InfoTags> info = new ArrayList();
            IndicarTagsActivity activity;
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                info.clear();
                for (DataSnapshot tag : _dataSnapshot.getChildren()) {
                    TagEntity t = tag.getValue(TagEntity.class);
                    InfoTags iTag = new InfoTags(t.getNombreTag(), false, tag.getKey());
                    info.add(iTag);
                }
                activity.mostrarTags(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }
            public ValueEventListener setActivity (Activity _activity)
            {
                activity=(IndicarTagsActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }
}
