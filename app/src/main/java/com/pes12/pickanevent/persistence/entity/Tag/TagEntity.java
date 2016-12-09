package com.pes12.pickanevent.persistence.entity.Tag;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagEntity {

    /*nombreTag*/
    private String nombreTag;
    private String id;

    public TagEntity() {

    }

    public TagEntity(String nombre) {
        setNombreTag(nombre);
    }

    public String getNombreTag() {
        return nombreTag;
    }

    public void setNombreTag(String _nombreTag) {
        nombreTag = _nombreTag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum ATTRIBUTES {

        NOMBRETAG("nombreTag");

        private String value;

        ATTRIBUTES(String _value) {
            value = _value;
        }

        ;

        public String getValue() {
            return value;
        }

        ;
    }
}
