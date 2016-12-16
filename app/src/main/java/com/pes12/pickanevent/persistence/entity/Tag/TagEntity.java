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

    public static final String NOMBRETABLA = "tags";

    public enum ATTRIBUTES {

        NOMBRETAG("nombreTag"),
        ID_GRUPOS("idGrupos"),
        ID("id");

        private String value;

        ATTRIBUTES(String _value) {
            value = _value;
        }

        public String getValue() {
            return value;
        }
    }

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

}