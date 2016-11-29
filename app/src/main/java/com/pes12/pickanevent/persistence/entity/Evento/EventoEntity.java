package com.pes12.pickanevent.persistence.entity.Evento;

import java.util.Date;

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
    private String dataInici;
    private String dataFinal;
    private String latitud;
    private String longitud;

    public EventoEntity()
    {

    }

    public EventoEntity(String nombre, String descr, String img, String precio, String webpage, String localizacion, String latitud, String longitud, Date dataInici, Date dataFinal){
        setTitulo(nombre);
        setDescripcion(descr);
        setImagen(img);
        setPrecio(precio);
        setWebpage(webpage);
        setLocalizacion(localizacion);
        setLatitud(latitud);
        setLongitud(longitud);
        setDataInici(dataInici);
        setDataFinal(dataFinal);
    }
    public boolean isEmpty() {return (titulo == null);}

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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Date getDataInici() {
        Date d = new Date(dataInici);
        return d;
    }

    public void setDataInici(Date dataInici) {
        String s = String.valueOf(dataInici);
        this.dataInici = s;
    }

    public Date getDataFinal() {
        Date d = new Date(dataFinal);
        return d;
    }

    public void setDataFinal(Date dataFinal) {
        String s = String.valueOf(dataFinal);
        this.dataFinal = s;
    }
}
