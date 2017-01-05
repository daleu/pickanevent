package com.pes12.pickanevent.business;

/**
 * Created by Clara on 21/10/2016.
 */

public class Info {
    public String img;
    public String primeraLinea;
    public String segonaLinea;
    public String textoBoton;
    public String id;
    public String tipus;

    public Info(String _img, String _primeraLinea, String _segonaLinea, String _textoBoton) {
        super();
        this.img = _img;
        this.primeraLinea = _primeraLinea;
        this.segonaLinea = _segonaLinea;
        this.textoBoton = _textoBoton;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String _img) {
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

    public String getId() { return id; }

    public void setId(String _id) {this.id = _id;}

    public String getTipus() { return tipus; }

    public void setTipus(String _tipus) {this.tipus = _tipus;}
}
