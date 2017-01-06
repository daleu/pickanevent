package com.pes12.pickanevent.business.ImagenGrupo;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.MGRFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

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


    public void subirImagen(InputStream _is, GrupoEntity _gg, String id) {
        UploadTask uploadTask = bdRefImagenes.child(id).putStream(_is);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            GrupoEntity grupo;
            String id;

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                grupo.setImagen(taskSnapshot.getDownloadUrl().toString());
                GrupoMGR gMGR = MGRFactory.getInstance().getGrupoMGR();
                System.out.println("actualitzo i la url es:  " + grupo.getImagen());
                gMGR.actualizar(id,grupo);
            }

            public OnSuccessListener setGrupo(GrupoEntity _gg,String _id) {
                grupo = _gg;
                id=_id;
                return this;
            }
        }.setGrupo(_gg,id));

    }
}
