package com.pes12.pickanevent.persistence.entity.Tag;

import java.util.Map;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagEntity {

    /*nombreTag*/
    private String nombreTag;
    private String id;
    private Map<String, String> idGrupos;

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

    public Map<String, String> getIdGrupos() {

        return idGrupos;
    }

    public void setIdGrupos(Map<String, String> idGrupos)
    {
        this.idGrupos = idGrupos;
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