package com.pes12.pickanevent.view;

import java.util.Enumeration;

/**
 * Created by p4 on 28/10/16.
 */

public class ViewUtils {

    //Aquesta classe incloura totes les funcions comunes que son utilitzades per mes de una classe de la capa view

    public static final String getNomMes(int m) {
        if (m==1) return "Enero";
        if (m==2) return "Febrero";
        if (m==3) return "Marzo";
        if (m==4) return "Abril";
        if (m==5) return "Mayo";
        if (m==6) return "Junio";
        if (m==7) return "Julio";
        if (m==8) return "Agosto";
        if (m==9) return "Setiembre";
        if (m==10) return "Octubre";
        if (m==11) return "Noviembre";
        if (m==12) return "Diciembre";
        return "Unknown";
    }
}
