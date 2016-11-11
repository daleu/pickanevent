package com.pes12.pickanevent.persistence.entity.Tag;

import com.pes12.pickanevent.business.Constantes;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagEntity {

    public TagEntity (){

    }

    public TagEntity (String nombre) {
        setNombreTag(nombre);
    }

    public enum ATTRIBUTES {

        NOMBRETAG   ("nombreTag");

        private String value;

        ATTRIBUTES(String _value){value=_value;};

        public String getValue(){return value;};
    }

    /*nombreTag*/
    private String nombreTag;
    public String getNombreTag() {return nombreTag;}
    public void setNombreTag(String _nombreTag) {
        nombreTag = _nombreTag;
    }
}
