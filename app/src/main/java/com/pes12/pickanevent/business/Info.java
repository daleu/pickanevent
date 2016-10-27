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

    public Bitmap getImg(){ return img;}
    public String getPrimeraLinea() {return primeraLinea;}
    public String getSegonaLinea() {return segonaLinea;}

    public void setImg (Bitmap img) {this.img = img;}
    public void setPrimeraLinea (String primeraLinea) {this.primeraLinea = primeraLinea;}
    public void setSegonaLinea (String segonaLinea) {this.segonaLinea = segonaLinea;}
}
