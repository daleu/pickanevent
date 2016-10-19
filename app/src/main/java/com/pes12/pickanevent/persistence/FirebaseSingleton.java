package com.pes12.pickanevent.persistence;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by usuario on 19/10/2016.
 */

public class FirebaseSingleton {

    private static FirebaseDatabase database;

    public FirebaseSingleton () {

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

    }

    public static FirebaseDatabase getInstance()
    {
        if(database==null)
        {
            new FirebaseSingleton();
            return database;
        }
        else return database;
    }
}
