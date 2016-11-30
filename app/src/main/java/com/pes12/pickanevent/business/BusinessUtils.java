package com.pes12.pickanevent.business;

import com.pes12.pickanevent.view.BaseActivity;

/**
 * Created by p4 on 28/10/16.
 */

public class BusinessUtils {

    //Aquesta classe incloura totes les funcions comunes que son utilitzades per mes de una classe de la capa business

    public static boolean permitUsr() {
        return !BaseActivity.getUsuarioActual().getCm();
    }

    public static boolean permitCm() {
        return BaseActivity.getUsuarioActual().getCm();
    }


}
