package com.pes12.pickanevent.persistence.entity.Tags;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagEntity {

    public TagEntity (){

    }

    public TagEntity (String nombre) {
        setNombreTag(nombre);
    }

    /*nombreTag*/
    private String nombreTag;
    public String getNombreTag() {return nombreTag;}
    public void setNombreTag(String _nombreTag) {
        nombreTag = _nombreTag;
    }
}
