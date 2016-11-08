package com.pes12.pickanevent.business.Tag;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
