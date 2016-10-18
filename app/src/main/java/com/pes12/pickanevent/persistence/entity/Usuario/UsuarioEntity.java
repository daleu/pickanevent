package com.pes12.pickanevent.persistence.entity.Usuario;

import com.pes12.pickanevent.persistence.entity.Grupo.GrupoEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioEntity {


    public UsuarioEntity ()
    {

    }

    public enum ATTRIBUTES {

        USERNAME    ("username"),
        NICKNAME    ("nickname"),
        PASSWORD    ("password"),
        EMAIL       ("email"),
        CM          ("cm");


        private String value;

        ATTRIBUTES(String _value){value=_value;};

        public String getValue(){return value;};
    }





    /*Username*/
    private String username;
    public  String getUsername() {return username;}
    public  void setUsername(String _username) {username = _username;}

    /*Nickname*/
    private String nickname;
    public  String getNickname() {return nickname;}
    public  void setNickname(String _nickname) {nickname = _nickname;}

    /*Password*/
    private String password;
    public  String getPassword() {return password;}
    public  void setPassword(String _password) {password = _password;}

    /*Email*/
    private String email;
    public  String getEmail() {return email;}
    public  void setEmail(String _email) {email = _email;}

    /*CM*/
    private Boolean cm;
    public  Boolean getCm() {return cm;}
    public  void setCm(Boolean _cm) {cm = _cm;}


    /*    RELACIONES   */

    /*GRUPOS, si cm==true serán los grupos creados, si cm == false serán los grupos seguidos */
    private Map<String,Boolean> idGrupos;
    public  Map<String,Boolean> getIdGrupos() {return idGrupos;}
    public  void setIdGrupos(Map<String,Boolean> _idGrupos){idGrupos = _idGrupos;}


    /*    EVENTOS   */
    private Map<String,Boolean> idEventos;
    public  Map<String,Boolean> getIdEventos() {return idEventos;}
    public  void setIdEventos(Map<String,Boolean> _idEventos){idEventos = _idEventos;}


    /*    TAGS   */
    private Map<String,Boolean> idTags;
    public  Map<String,Boolean> getIdTags() {return idTags;}
    public  void setIdTags(Map<String,Boolean> _idTags){idTags = _idTags;}

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "\nusername='" + username + '\'' +
                "\n, nickname='" + nickname + '\'' +
                "\n, password='" + password + '\'' +
                "\n, email='" + email + '\'' +
                "\n, cm=" + cm +
                "\n, idGrupos=" + idGrupos +
                "\n, idEventos=" + idEventos +
                "\n, idTags=" + idTags +
                '}';
    }
}
