package com.unl.estrdts.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.unl.estrdts.base.controller.dao.dao_models.DaoCancion;
import com.unl.estrdts.base.models.TipoArchivoEnum;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class CancionServices {
    private DaoCancion db;

    public CancionServices() {
        db = new DaoCancion();
    }

    public List<String> listTipo() {
        return Arrays.stream(TipoArchivoEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public void create(@NotEmpty String nombre, Integer idGenero, Integer duracion,
            @NotEmpty String url, @NotEmpty String tipo, Integer idAlbum) throws Exception {
        if (nombre.trim().length() > 0 && url.trim().length() > 0 &&
                tipo.trim().length() > 0 && idGenero > 0 && idAlbum > 0) {
            int nextId = 1;
            var canciones = db.listAll();
            for (int i = 0; i < canciones.getLength(); i++) {
                var c = canciones.get(i);
                if (c.getId() != null && c.getId() >= nextId) {
                    nextId = c.getId() + 1;
                }
            }
            db.getObj().setId(nextId);
            db.getObj().setNombre(nombre);
            db.getObj().setDuracion(duracion);
            db.getObj().setIdAlbum(idAlbum);
            db.getObj().setIdGenero(idGenero);
            db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
            db.getObj().setUrl(url);
            if (!db.save())
                throw new Exception("No se pudo guardar los datos de la canción");
        }
    }

    public void update(Integer id, @NotEmpty String nombre, Integer idGenero, Integer duracion, @NotEmpty String url,
            @NotEmpty String tipo, Integer idAlbum) throws Exception {
        if (nombre.trim().length() > 0 && url.trim().length() > 0 && tipo.trim().length() > 0
                && idGenero > 0 && idAlbum > 0) {
            db.setObj(db.listAll().get(id - 1));
            db.getObj().setNombre(nombre);
            db.getObj().setDuracion(duracion);
            db.getObj().setIdAlbum(idAlbum);
            db.getObj().setIdGenero(idGenero);
            db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
            db.getObj().setUrl(url);
            if (!db.update(id - 1))
                throw new Exception("No se pudo modificar los datos de la canción");
        }
    }

    public List<HashMap<String, String>> ordenar(String atributo, Integer type) throws Exception {
        List<HashMap<String, String>> result = new ArrayList<>();
        var linkList = db.orderQ(type, atributo);
        for (int i = 0; i < linkList.getLength(); i++) {
            try {
                result.add(linkList.get(i));
            } catch (Exception e) {
            }
        }
        return result;
    }

    public List<HashMap<String, String>> listCancion() {
        List<HashMap<String, String>> result = new ArrayList<>();
        try {
            var linkList = db.all();
            for (int i = 0; i < linkList.getLength(); i++) {
                result.add(linkList.get(i));
            }
        } catch (Exception e) {
        }
        return result;
    }

    public void delete(Integer id) throws Exception {
        if (id == null || id <= 0)
            throw new Exception("ID inválido");
        boolean ok = db.deleteById(id);
        if (!ok)
            throw new Exception("No se pudo eliminar la canción");

        var canciones = db.listAll();
        for (int i = 0; i < canciones.getLength(); i++) {
            var c = canciones.get(i);
            c.setId(i + 1);
        }

        db.persistAll(canciones);
    }

    public List<HashMap<String, String>> search(String atributo, String texto, Integer type) throws Exception {
        List<HashMap<String, String>> result = new ArrayList<>();
        var linkList = db.search(type, texto, atributo);
        for (int i = 0; i < linkList.getLength(); i++) {
            result.add(linkList.get(i));
        }
        return result;
    }
}
