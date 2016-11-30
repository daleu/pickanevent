package com.pes12.pickanevent.view;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Enumeration;
import com.pes12.pickanevent.R;
import com.pes12.pickanevent.persistence.entity.Evento.EventoEntity;

/**
 * Created by p4 on 28/10/16.
 */

public class ViewSharedMethods {

    //Aquesta classe incloura totes les funcions comunes que son utilitzades per mes de una classe de la capa view

    public static final String getNomMes(int m, Context _context) {
        if (m==1) return _context.getString(R.string.MES_ENERO);
        if (m==2) return _context.getString(R.string.MES_FEBRERO);
        if (m==3) return _context.getString(R.string.MES_MARZO);
        if (m==4) return _context.getString(R.string.MES_ABRIL);
        if (m==5) return _context.getString(R.string.MES_MAYO);
        if (m==6) return _context.getString(R.string.MES_JUNIO);
        if (m==7) return _context.getString(R.string.MES_JULIO);
        if (m==8) return _context.getString(R.string.MES_AGOSTO);
        if (m==9) return _context.getString(R.string.MES_SETIEMBRE);
        if (m==10) return _context.getString(R.string.MES_OCTUBRE);
        if (m==11) return _context.getString(R.string.MES_NOVIEMBRE);
        if (m==12) return _context.getString(R.string.MES_DICIEMBRE);
        return "Unknown";
    }
}
