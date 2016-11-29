package com.pes12.pickanevent.persistence.entity.Grupo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class GrupoEntity {

    public GrupoEntity ()
    {

    }

    public GrupoEntity (String nombre, String descr, String img, String nick, String idTagGeneral, String idtag, String idUs) {
        setNombreGrupo(nombre);
        setDescripcion(descr);
        setImagen(img);
        setNickname(nick);
        setIdTagGeneral(idTagGeneral);
        setIdTagGeneral(idtag);
        setIdUsuario(idUs);

    }


    public enum ATTRIBUTES {

        NOMBREGRUPO   ("nombreGrupo"),
        DESCRIPCION ("descripcion"),
        IMAGEN      ("imagen"),
        NICKNAME    ("nickname"),
        IDTAG       ("idtag"),
        IDUSUARIO   ("idusuario"), //creador
        WEBPAGE     ("webpage");


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
    public  void setNickname(String _nickname) {nickname = _nickname;}

    /*idTag General*/
    private String idTagGeneral;
    public  String getidTagGeneral() {return idTagGeneral;}
    public  void setIdTagGeneral(String _idTagGeneral) {idTagGeneral = _idTagGeneral;}

    /*idUsuario*/
    private String idUsuario;
    public  String getIdUsuario() {return idUsuario;}
    public  void setIdUsuario(String _idUsuario) {idUsuario = _idUsuario;}

    /*webPage*/
    private String webpage;
    public String getWebpage() {
        return webpage;
    }
    public void setWebpage(String _webpage) {
        this.webpage = _webpage;
    }




    /*    RELACIONES   */


    /*    EVENTOS   */
    private Map<String,String> mapIdEventos;
    public  Map<String,String> getIdEventos() {return mapIdEventos;}
    public  void setIdEventos(Map<String,String> _idEventos){mapIdEventos = _idEventos;}



    /*    TAGS_ADICIONALES   */
    private Map<String,String> mapIdTags;
    public  Map<String,String> getIdTags() {return mapIdTags;}
    public  void setIdTags(Map<String,String> _idTags){mapIdTags = _idTags;}


}
