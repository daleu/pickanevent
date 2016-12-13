package com.pes12.pickanevent.persistence.entity.Usuario;

import java.util.Map;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioEntity {

    private String id;
    private String bio;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private Boolean cm;
    /*GRUPOS, si cm==true serán los grupos creados, si cm == false serán los grupos seguidos */
    private Map<String, String> idGrupos;
    private Map<String, String> idEventos;
    private Map<String, String> idTags;
    private Map<String, String> idUsuarios;

    public static final String NOMBRETABLA = "usuarios";

    public enum ATTRIBUTES {

        USERNAME("username"),
        BIO("bio"),
        ID("id"),
        NICKNAME("nickname"),
        PASSWORD("password"),
        EMAIL("email"),
        CM("cm"),
        ID_GRUPOS("idGrupos"),
        ID_EVENTOS("idEventos"),
        ID_TAGS("idTags"),
        ID_USUARIOS("idUsuarios");

        private String value;

        ATTRIBUTES(String _value) {
            value = _value;
        };

        public String getValue() {
            return value;
        };
    }

    public UsuarioEntity() {

    }

    public UsuarioEntity(String nickname, Boolean cm) {

        this.nickname = nickname;

        this.cm = cm;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        username = _username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String _nickname) {
        nickname = _nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public String getEmail() {
        return email;
    }


    /*    RELACIONES   */

    public void setEmail(String _email) {
        email = _email;
    }

    public Boolean getCm() {
        return cm;
    }

    public void setCm(Boolean _cm) {
        cm = _cm;
    }

    public Map<String, String> getIdGrupos() {
        return idGrupos;
    }

    public void setIdGrupos(Map<String, String> _idGrupos) {
        idGrupos = _idGrupos;
    }

    public Map<String, String> getIdEventos() {
        return idEventos;
    }

    public void setIdEventos(Map<String, String> _idEventos) {
        idEventos = _idEventos;
    }

    public Map<String, String> getIdTags() {
        return idTags;
    }

    public void setIdTags(Map<String, String> _idTags) {
        idTags = _idTags;
    }

    public Map<String, String> getIdUsuarios() {
        return idUsuarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdUsuarios(Map<String, String> _idUsuarios) {
        idUsuarios = _idUsuarios;
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "bio='" + bio + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", cm=" + cm +
                ", idGrupos=" + idGrupos +
                ", idEventos=" + idEventos +
                ", idTags=" + idTags +
                ", idUsuarios=" + idUsuarios +
                '}';
    }
}
