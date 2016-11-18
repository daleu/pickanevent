package com.pes12.pickanevent.business;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.ImagenPerfilUsuario.ImagenPerfilUsuarioMGR;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.FirebaseFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

/**
 * Created by Clara on 23/10/2016.
 */

public class MGRFactory {

    private final EventoMGR eventoMGR;
    private final GrupoMGR grupoMGR;
    private final UsuarioMGR usuarioMGR;
    private final TagMGR tagMGR;
    private final ImagenPerfilUsuarioMGR ipuMGR;
    private final FirebaseDatabase databse;
    private final FirebaseStorage storage;
    private static MGRFactory mgrFactory;


    private MGRFactory() {
        eventoMGR = new EventoMGR();
        grupoMGR = new GrupoMGR();
        usuarioMGR = new UsuarioMGR();
        tagMGR = new TagMGR();
        ipuMGR = new ImagenPerfilUsuarioMGR();
        databse = FirebaseFactory.getInstance();
        storage= FirebaseStorage.getInstance();

        eventoMGR.inicializarDatabase(databse);
        grupoMGR.inicializarDatabase(databse);
        usuarioMGR.inicializarDatabase(databse);
        tagMGR.inicializarDatabase(databse);
        ipuMGR.inicializarDatabase(storage);
    }

    public static MGRFactory getInstance() {
        if (mgrFactory == null) {
            mgrFactory = new MGRFactory();
        }
        return mgrFactory;
    }

    public GrupoMGR getGrupoMGR() {
        return grupoMGR;
    }

    public EventoMGR getEventoMGR() {
        return eventoMGR;
    }

    public UsuarioMGR getUsuarioMGR() {
        return usuarioMGR;
    }

    public TagMGR getTagMGR() {
        return tagMGR;
    }

    public ImagenPerfilUsuarioMGR getImagenPerfilUsuarioMGR() {
        return ipuMGR;
    }
}
