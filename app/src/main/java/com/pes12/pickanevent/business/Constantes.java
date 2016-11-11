package com.pes12.pickanevent.business;

/**
 * Created by p4 on 8/11/16.
 */

public final class Constantes {


    /* CAMPOS DE LA BASE DE DATOS */
    public static final String BBDD_TABLA_EVENTOS = "eventos";
    public static final String BBDD_TABLA_GRUPOS = "grupos";
    public static final String BBDD_TABLA_TAGS = "tags";
    public static final String BBDD_TABLA_USUARIOS = "usuarios";
    public static final String BBDD_ATRIBUTO_NOMBRE_EVENTO = "nombreEvento";
    public static final String BBDD_ATRIBUTO_NOMBRE_GRUPO = "nombreGrupo";
    public static final String BBDD_ATRIBUTO_NOMBRE_TAG = "nombreTag";
    public static final String BBDD_ATRIBUTO_NOMBRE_USUARIO = "username";

    /* PRINT MESSAGES */
    /* ERROR MESSAGES */
    public static final String ERROR_EXISTE_GRUPO = "Ya existe un grupo con ese nombre.";
    public static final String ERROR_EXISTE_USUARIO = "Ya existe un usuario con ese nombre.";
    public static final String ERROR_EXISTE_TAG = "Ya existe un tag con ese nombre.";
    public static final String ERROR_INESPERADO = "Error inesperado.";
    public static final String ERROR_CREAR_USUARIO = "Error inesperado al crear usuario";

    /* LOG MESSAGES */
    public static final String LOG_LOGIN_CORRECTO = "Login correcto.";
    public static final String LOG_LOGIN_INCORRECTO = "Login incorrecto.";
    public static final String LOG_USUARI_CREADO_CORRECTO = "Usuario creado correctamente";

}
