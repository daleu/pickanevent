package com.pes12.pickanevent.business;

import android.graphics.Bitmap;

/**
 * Created by Clara on 21/10/2016.
 */

public class Info {
    public Bitmap img;
    public String primeraLinea;
    public String segonaLinea;

    public Info(Bitmap img, String primeraLinea, String segonaLinea) {
        super();
        this.img = img;
        this.primeraLinea = primeraLinea;
        this.segonaLinea = segonaLinea;
    }
}
