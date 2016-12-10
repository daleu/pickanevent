package com.pes12.pickanevent.persistence.entity.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clara on 08/11/2016.
 */

public class TagEntity {

    /*nombreTag*/
    private String nombreTag;
    private Map<String,String> mapIdGrupos;

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

    public Map<String,String> getIdGrupos() {return mapIdGrupos;}

    public void setIdGrupos (Map<String,String> _mapIdGrupos) {mapIdGrupos = _mapIdGrupos;}

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
