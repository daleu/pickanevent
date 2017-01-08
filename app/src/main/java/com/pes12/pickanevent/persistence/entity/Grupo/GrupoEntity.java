package com.pes12.pickanevent.persistence.entity.Grupo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoEntity implements Serializable{

    private String nombreGrupo;
    private String descripcion;
    private String imagen;
    private String nickname;
    private String idTagGeneral;
    private String idUsuario;
    private String webpage;
    private Map<String, String> mapIdEventos;
    private Map<String, String> mapIdTags;
    //private String id;

    public static final String NOMBRETABLA ="grupos";

    public enum ATTRIBUTES {

        NOMBREGRUPO("nombreGrupo"),
        DESCRIPCION("descripcion"),
        IMAGEN("imagen"),
        NICKNAME("nickname"),
        IDTAGGENERAL("idTagGeneral"),
        IDUSUARIO("idUsuario"), //creador
        WEBPAGE("webpage"),
        MAP_ID_EVENTOS("mapIdEventos"),
        MAP_ID_TAGS("mapIdTags"),
        ID("id");

        private String value;

        ATTRIBUTES(String _value) {
            value = _value;
        }

        public String getValue() {
            return value;
        }
    }

    public GrupoEntity() {

    }

    public GrupoEntity(String nombre, String descr, String img, String nick, String idTagGeneral, String idtag, String idUs) {
        setNombreGrupo(nombre);
        setDescripcion(descr);
        setImagen(img);
        setNickname(nick);
        setIdTagGeneral(idTagGeneral);
        setIdTagGeneral(idtag);
        setIdUsuario(idUs);

    }

    public GrupoEntity(String nombre, String descr) {
        setNombreGrupo(nombre);
        setDescripcion(descr);
    }

    public GrupoEntity(String nombre, String descr, String img) {
        setNombreGrupo(nombre);
        setDescripcion(descr);
        setImagen(img);

    }


    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String _groupname) {
        nombreGrupo = _groupname;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String _descripcion) {
        descripcion = _descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String _imagen) {
        imagen = _imagen;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String _nickname) {
        nickname = _nickname;
    }

    public String getidTagGeneral() {
        return idTagGeneral;
    }

    public void setIdTagGeneral(String _idTagGeneral) {
        idTagGeneral = _idTagGeneral;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String _idUsuario) {
        idUsuario = _idUsuario;
    }

    public String getWebpage() {
        return webpage;
    }






    /*    RELACIONES   */

    public void setWebpage(String _webpage) {
        this.webpage = _webpage;
    }

    public Map<String, String> getIdEventos() {
        return mapIdEventos;
    }

    public void setIdEventos(Map<String, String> _idEventos) {
        mapIdEventos = _idEventos;
    }

    public Map<String, String> getIdTags() {
        return mapIdTags;
    }

    public void setIdTags(Map<String, String> _idTags) {
        mapIdTags = _idTags;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/


}
