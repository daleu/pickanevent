package com.pes12.pickanevent.business;

import android.graphics.Bitmap;

/**
 * Created by Clara on 21/10/2016.
 */

public class Info {
    public Bitmap img;
    public String primeraLinea;
    public String segonaLinea;
    public String textoBoton;

    public Info(Bitmap _img, String _primeraLinea, String _segonaLinea, String _textoBoton) {
        super();
        this.img = _img;
        this.primeraLinea = _primeraLinea;
        this.segonaLinea = _segonaLinea;
        this.textoBoton = _textoBoton;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap _img) {
        this.img = _img;
    }

    public String getPrimeraLinea() {
        return primeraLinea;
    }

    public void setPrimeraLinea(String _primeraLinea) {
        this.primeraLinea = _primeraLinea;
    }

    public String getSegonaLinea() {
        return segonaLinea;
    }

    public void setSegonaLinea(String _segonaLinea) {
        this.segonaLinea = _segonaLinea;
    }

    public String getTextoBoton() {
        return textoBoton;
    }

    public void setTextoBoton(String _textoBoton) {
        this.textoBoton = _textoBoton;
    }
}
