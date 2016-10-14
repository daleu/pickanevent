package com.pes12.pickanevent.persistence.entity.Usuario;

/**
 * Created by Legault on 14/10/2016.
 */

public class UsuarioEntity {


    public UsuarioEntity ()
    {
        id      =0l;
        username="";
        nickname="";
        password="";
        email   ="";
        cm      =false;
    }

    public enum ATTRIBUTES {
        ID          ("id"),
        USERNAME    ("username"),
        NICKNAME    ("nickname"),
        PASSWORD    ("password"),
        EMAIL       ("email"),
        CM          ("cm");


        private String value;

        ATTRIBUTES(String _value){value=_value;};

        public String getValue(){return value;};
    }


    /*ID*/
    private Long id;
    public  Long getId() {return id;}
    public  void setId(Long _id) {id = _id;}

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
    private boolean cm;
    public  boolean getCm() {return cm;}
    public  void setCm(boolean _cm) {cm = _cm;}



}
