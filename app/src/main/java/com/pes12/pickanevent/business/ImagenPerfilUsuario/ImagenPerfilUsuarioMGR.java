package com.pes12.pickanevent.business.ImagenPerfilUsuario;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes12.pickanevent.view.BaseActivity;
import com.pes12.pickanevent.view.CrearUsuarioActivity;
import com.pes12.pickanevent.view.MainActivity;
import com.pes12.pickanevent.view.PerfilUsuarioActivity;

import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Legault on 18/11/2016.
 */

public class ImagenPerfilUsuarioMGR {

    private FirebaseStorage storage;
    private StorageReference bdRefImagenes;

    public void inicializarDatabase(FirebaseStorage _storage) {

        bdRefImagenes = _storage.getReference("ImagenPerfilUsuario");

    }


    public void subirImagen(InputStream _is, FirebaseUser _user) {
        UploadTask uploadTask = bdRefImagenes.child(_user.getUid()).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            FirebaseUser user;

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(taskSnapshot.getDownloadUrl())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
            }

            public OnSuccessListener setUser(FirebaseUser _user) {
                user = _user;
                return this;
            }
        }.setUser(_user));

    }

    public void subirImagenPerfil(InputStream _is, FirebaseUser _user,Activity _ba) {
        UploadTask uploadTask = bdRefImagenes.child(_user.getUid()).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            FirebaseUser user;
            PerfilUsuarioActivity activity;
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(taskSnapshot.getDownloadUrl())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    activity.mostrarInfoUsuario();
                                }
                            }
                        });
            }

            public OnSuccessListener setUserAndActivity(FirebaseUser _user,Activity _activity) {
                user = _user;
                activity = (PerfilUsuarioActivity) _activity;
                return this;
            }


        }.setUserAndActivity(_user,_ba));

    }


    public void subirImagenCreacionUsuario(InputStream _is, FirebaseUser _user, Activity _activity) {
        UploadTask uploadTask = bdRefImagenes.child(_user.getUid()).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            FirebaseUser user;
            CrearUsuarioActivity activity;
            String img;
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                img = taskSnapshot.getDownloadUrl().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(taskSnapshot.getDownloadUrl())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    activity.addImagenPorDefecto(img);
                                }
                            }
                        });

            }

            public OnSuccessListener setUserAndActivity(FirebaseUser _user,Activity _activity) {
                user = _user;
                activity = (CrearUsuarioActivity) _activity;
                return this;
            }


        }.setUserAndActivity(_user,_activity));
    }
}
