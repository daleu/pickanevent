package com.pes12.pickanevent.business.Tag;

import android.app.Activity;
import android.nfc.Tag;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pes12.pickanevent.business.Constantes;
import com.pes12.pickanevent.business.Info;
import com.pes12.pickanevent.business.InfoTags;
import com.pes12.pickanevent.persistence.entity.Tag.TagEntity;
import com.pes12.pickanevent.view.BuscarActivity;
import com.pes12.pickanevent.view.IndicarTagsActivity;
import com.pes12.pickanevent.view.VerGruposConTagActivity;
import com.pes12.pickanevent.view.VerGruposCreadosActivity;
import com.pes12.pickanevent.view.VerInfoGrupoActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public String crear(final TagEntity _entity) {
        bdRefTags.orderByChild(Constantes.BBDD_ATRIBUTO_NOMBRE_TAG).equalTo(_entity.getNombreTag()).addListenerForSingleValueEvent(new ValueEventListener() {
            TagEntity ent;

            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_TAG);
                } else {
                    DatabaseReference tag = bdRefTags.push();
                    tag.setValue(ent);
                    tag.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setEntity(TagEntity _ent) {
                ent = _ent;
                return this;
            }
        }.setEntity(_entity));
        return "";
    }

    public void getInfoTag(Activity _activity, Map<String, String> _idS) {
        bdRefTags.orderByKey().addValueEventListener(new ValueEventListener() {
            ArrayList<String> info = new ArrayList();
            VerInfoGrupoActivity activity;
            Map<String, String> idS;

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

            public ValueEventListener setActivity(Activity _activity, Map<String, String> _idS) {
                activity = (VerInfoGrupoActivity) _activity;
                idS = _idS;
                return this;
            }
        }.setActivity(_activity, _idS));
    }

    public void getInfoTagGrupos(Activity _activity, final String _tagName) {
        bdRefTags.orderByKey().addValueEventListener(new ValueEventListener() {
            TagEntity info = new TagEntity();
            VerGruposConTagActivity activity;
            String tagName;

            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {

                for (DataSnapshot tag : _dataSnapshot.getChildren()) {
                    TagEntity t = tag.getValue(TagEntity.class);
                    if (t.getNombreTag() == tagName) {

                        info = t;
                    }
                }
                activity.mostrarGruposTag(info);
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {
                System.out.println(Constantes.ERROR_INESPERADO);
            }

            public ValueEventListener setActivity(Activity _activity, String _tagName) {
                activity = (VerGruposConTagActivity) _activity;
                tagName = _tagName;
                return this;
            }
        }.setActivity(_activity, _tagName));
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

            public ValueEventListener setActivity(Activity _activity) {
                activity = (IndicarTagsActivity) _activity;
                return this;
            }
        }.setActivity(_activity));
    }

    public String crearNuevoTag(Activity _activity, final TagEntity _entity) {
        bdRefTags.orderByChild(Constantes.BBDD_ATRIBUTO_NOMBRE_TAG).equalTo(_entity.getNombreTag()).addListenerForSingleValueEvent(new ValueEventListener() {
            TagEntity ent;
            IndicarTagsActivity activity;
            String id;
            @Override
            public void onDataChange(DataSnapshot _snapshot) {
                if (_snapshot.getValue() != null) {
                    System.out.println(Constantes.ERROR_EXISTE_TAG);
                } else {
                    DatabaseReference tag = bdRefTags.push();
                    tag.setValue(ent);
                    id = tag.getKey();
                }
                activity.checkNuevoTag(id, _entity.getNombreTag());
            }

            @Override
            public void onCancelled(DatabaseError _arg0) {
            }

            public ValueEventListener setActivity(Activity _activity, TagEntity _tag) {
                activity = (IndicarTagsActivity) _activity;
                ent = _tag;
                return this;
            }
        }.setActivity(_activity, _entity));
        return "";
    }

    public void getTagsByName(Activity _activity, String _text) {


        Query queryRef = bdRefTags.orderByChild(TagEntity.ATTRIBUTES.NOMBRETAG.getValue()).startAt(_text).endAt(_text + "\uf8ff");

        queryRef.addValueEventListener(new ValueEventListener() {
            IndicarTagsActivity activity;
            Map<String, TagEntity> map = new LinkedHashMap<String, TagEntity>();

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<InfoTags> n = new ArrayList<InfoTags>();
                for (DataSnapshot tag : snapshot.getChildren()) {
                    n.add(new InfoTags(tag.getValue(TagEntity.class).getNombreTag(), false, tag.getKey()));

                }
                activity.mostrarResultadosBusquedaTags(n);
                activity.hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError _databaseError) {

            }

            public ValueEventListener setActivity(Activity _activity) {
                activity = (IndicarTagsActivity) _activity;
                return this;
            }

        }.setActivity(_activity));
    }


}
