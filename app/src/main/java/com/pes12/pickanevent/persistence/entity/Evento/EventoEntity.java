package com.pes12.pickanevent.persistence.entity.Evento;

/**
 * Created by aleue on 21/10/2016.
 */

public class EventoEntity {

    private String titulo;
    private String descripcion;
    private String imagen;
    private String precio;
    private String webpage;
    private String localizacion;
    private String horario;


    public EventoEntity()
    {

    }

    public EventoEntity(String nombre, String descr, String img, String precio, String webpage, String localizacion, String horario){
        setTitulo(nombre);
        setDescripcion(descr);
        setImagen(img);
        setPrecio(precio);
        setWebpage(webpage);
        setLocalizacion(localizacion);
        setHorario(horario);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
