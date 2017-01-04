package com.pes12.pickanevent.business.ImagenEvento;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.view.PerfilUsuarioActivity;

import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Legault on 18/11/2016.
 */

public class ImagenEventoMGR {

    private FirebaseStorage storage;
    private StorageReference bdRefImagenes;

    public void inicializarDatabase(FirebaseStorage _storage) {

        bdRefImagenes = _storage.getReference("ImagenEvento");

    }


    public void subirImagen(InputStream _is, EventoEntity _ee, String id) {
        UploadTask uploadTask = bdRefImagenes.child(id).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            EventoEntity evento;
            String id;

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                evento.setImagen(taskSnapshot.getDownloadUrl().toString());
                EventoMGR eMGR = MGRFactory.getInstance().getEventoMGR();
                eMGR.actualizar(id,evento);
            }

            public OnSuccessListener setEvento(EventoEntity _ee,String _id) {
                evento = _ee;
                id=_id;
                return this;
            }
        }.setEvento(_ee,id));

    }
}
