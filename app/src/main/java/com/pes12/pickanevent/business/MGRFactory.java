package com.pes12.pickanevent.business;

import com.google.firebase.database.FirebaseDatabase;
import com.pes12.pickanevent.business.Evento.EventoMGR;
import com.pes12.pickanevent.business.Grupo.GrupoMGR;
import com.pes12.pickanevent.business.Tag.TagMGR;
import com.pes12.pickanevent.business.Usuario.UsuarioMGR;
import com.pes12.pickanevent.persistence.FirebaseFactory;
import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

/**
 * Created by Clara on 23/10/2016.
 */

public class MGRFactory {

    public final EventoMGR eventoMGR;
    public final GrupoMGR grupoMGR;
    public final UsuarioMGR usuarioMGR;
    public final TagMGR tagMGR;
    public final FirebaseDatabase databse;
    public static MGRFactory mgrFactory;


    private MGRFactory() {
        eventoMGR = new EventoMGR();
        grupoMGR = new GrupoMGR();
        usuarioMGR = new UsuarioMGR();
        tagMGR = new TagMGR();
        databse = FirebaseFactory.getInstance();
        eventoMGR.inicializarDatabase(databse);
        grupoMGR.inicializarDatabase(databse);
        usuarioMGR.inicializarDatabase(databse);
        tagMGR.inicializarDatabase(databse);
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
}
