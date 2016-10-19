package com.pes12.pickanevent.persistence.entity.Grupo;

import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoEntity {

    public GrupoEntity ()
    {

    }

    public enum ATTRIBUTES {

        NOMBREGRUPO   ("nombregrupo"),
        DESCRIPCION ("descripcion"),
        IMAGEN      ("imagen"),
        NICKNAME    ("nickname"),
        IDTAG       ("idtag"),
        IDUSUARIO   ("idusuario"); //creador


        private String value;

        ATTRIBUTES(String _value){value=_value;};

        public String getValue(){return value;};
    }


    /*NombreGrupo*/
    private String nombreGrupo;
    public  String getNombreGrupo() {return nombreGrupo;}
    public  void setNombreGrupo(String _groupname) {nombreGrupo = _groupname;}

    /*Descripcion*/
    private String descripcion;
    public  String getDescripcion() {return descripcion;}
    public  void setDescripcion(String _descripcion) {descripcion = _descripcion;}

    /*Imagen*/
    private String imagen;
    public  String getImagen() {return imagen;}
    public  void setImagen(String _imagen) {imagen = _imagen;}

    /*Nickname*/
    private String nickname;
    public  String getNickname() {return nickname;}
    public  void setNickname(String _nickname) {nombreGrupo = _nickname;}

    /*idTag General*/
    private String idTagGeneral;
    public  String getidTagGeneral() {return idTagGeneral;}
    public  void setIdTagGeneral(String _idTagGeneral) {idTagGeneral = _idTagGeneral;}

    /*idUsuario*/
    private String idUsuario;
    public  String getIdUsuario() {return idUsuario;}
    public  void setIdUsuario(String _idUsuario) {nombreGrupo = _idUsuario;}


    /*    RELACIONES   */


    /*    EVENTOS   */
    private Map<String,Boolean> idEventos;
    public  Map<String,Boolean> getIdEventos() {return idEventos;}
    public  void setIdEventos(Map<String,Boolean> _idEventos){idEventos = _idEventos;}


    /*    TAGS_ADICIONALES   */
    private Map<String,Boolean> idTags;
    public  Map<String,Boolean> getIdTags() {return idTags;}
    public  void setIdTags(Map<String,Boolean> _idTags){idTags = _idTags;}


}