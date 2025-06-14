package com.unl.estrdts.base.models;

public class Cancion {
    private Integer id;
    private String nombre;
    private Integer idGenero;
    private Integer duracion;
    private String url;
    private TipoArchivoEnum tipo;
    private Integer idAlbum;

    // getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Integer idGenero) {
        this.idGenero = idGenero;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TipoArchivoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoArchivoEnum tipo) {
        this.tipo = tipo;
    }

    public Integer getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

}
