package com.pes12.pickanevent.persistence;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by usuario on 19/10/2016.
 */

public class FirebaseFactory {

    private static FirebaseDatabase database;

    private FirebaseFactory() {

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

    }

    public static FirebaseDatabase getInstance() {
        if (database == null) {
            new FirebaseFactory();
            return database;
        } else return database;
    }
}
