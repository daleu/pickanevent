package com.pes12.pickanevent.business.ImagenGrupo;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;
import com.pes12.pickanevent.view.CrearGrupoActivity;
import com.pes12.pickanevent.view.EditarGrupoActivity;

import java.io.InputStream;

/**
 * Created by Clara on 04/01/2017.
 */

public class ImagenGrupoMGR {
    private FirebaseStorage storage;
    private StorageReference bdRefImagenes;

    public void inicializarDatabase(FirebaseStorage _storage) {

        bdRefImagenes = _storage.getReference("ImagenGrupo");

    }


    public void subirImagen(InputStream _is, GrupoEntity _gg, String id, Activity _activity) {
        UploadTask uploadTask = bdRefImagenes.child(id).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            GrupoEntity grupo;
            String id;
            CrearGrupoActivity activity;

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                grupo.setImagen(taskSnapshot.getDownloadUrl().toString());
                GrupoMGR gMGR = MGRFactory.getInstance().getGrupoMGR();
                gMGR.actualizar(id,grupo);
                activity.redireccionarConIdGrupo(id);

            }

            public OnSuccessListener setGrupo(GrupoEntity _gg,String _id, Activity _activity) {
                grupo = _gg;
                id=_id;
                activity = (CrearGrupoActivity) _activity;
                return this;
            }
        }.setGrupo(_gg,id, _activity));

    }

    public void subirImagenAlEditar(InputStream _is, GrupoEntity _gg, String id, Activity _activity) {
        UploadTask uploadTask = bdRefImagenes.child(id).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            GrupoEntity grupo;
            String id;
            EditarGrupoActivity activity;

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                grupo.setImagen(taskSnapshot.getDownloadUrl().toString());
                GrupoMGR gMGR = MGRFactory.getInstance().getGrupoMGR();
                gMGR.actualizar(id,grupo);
                activity.redireccionar();

            }

            public OnSuccessListener setGrupo(GrupoEntity _gg,String _id, Activity _activity) {
                grupo = _gg;
                id=_id;
                activity = (EditarGrupoActivity) _activity;
                return this;
            }
        }.setGrupo(_gg,id, _activity));

    }
}
