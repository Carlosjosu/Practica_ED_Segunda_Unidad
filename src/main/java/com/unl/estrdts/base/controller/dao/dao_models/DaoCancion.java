package com.unl.estrdts.base.controller.dao.dao_models;

import java.util.HashMap;

import com.unl.estrdts.base.controller.DataStruc.List.Linkendlist;
import com.unl.estrdts.base.controller.Util.Utiles;
import com.unl.estrdts.base.controller.dao.AdapterDao;
import com.unl.estrdts.base.models.Cancion;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;

    public DaoCancion() {
        super(Cancion.class);
    }

    public Cancion getObj() {
        if (obj == null) {
            this.obj = new Cancion();
        }
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            this.persist(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Linkendlist<HashMap<String, String>> all() throws Exception {
        Linkendlist<HashMap<String, String>> lista = new Linkendlist<>();
        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    public HashMap<String, String> toDict(Cancion cancion) {
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", String.valueOf(cancion.getId()));
        aux.put("nombre", cancion.getNombre());
        aux.put("duracion", String.valueOf(cancion.getDuracion()));
        aux.put("idAlbum", String.valueOf(cancion.getIdAlbum()));
        aux.put("idGenero", String.valueOf(cancion.getIdGenero()));
        aux.put("tipo", String.valueOf(cancion.getTipo()));
        aux.put("url", cancion.getUrl());
        return aux;
    }

    private int partition(HashMap<String, String> arr[], int begin, int end, Integer type, String attribute) {
        HashMap<String, String> pivot = arr[end];
        int i = begin - 1;

        if (type == (Utiles.ASCENDENTE)) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().toLowerCase()
                        .compareTo(pivot.get(attribute).toString().toLowerCase()) < 0) {
                    i++;
                    HashMap<String, String> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().toLowerCase()
                        .compareTo(pivot.get(attribute).toString().toLowerCase()) > 0) {
                    i++;
                    HashMap<String, String> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, String> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;
        return i + 1;
    }

    private void quickSort(HashMap<String, String> arr[], int begin, int end, Integer type, String attribute) {
        if (begin < end) {
            int pivotIndex = partition(arr, begin, end, type, attribute);
            quickSort(arr, begin, pivotIndex - 1, type, attribute);
            quickSort(arr, pivotIndex + 1, end, type, attribute);
        }
    }

    public Linkendlist<HashMap<String, String>> orderQ(Integer type, String attribute) throws Exception {
        Linkendlist<HashMap<String, String>> lista = all();
        if (!all().isEmpty()) {
            HashMap<String, String> arr[] = all().toArray();
            quickSort(arr, 0, arr.length - 1, type, attribute);
            lista.toList(arr);
        }
        return lista;
    }

    public Linkendlist<HashMap<String, String>> search(Integer type, String text, String attribute) throws Exception {
        Linkendlist<HashMap<String, String>> lista = all();
        Linkendlist<HashMap<String, String>> result = new Linkendlist<>();

        if (!lista.isEmpty()) {
            lista = orderQ(type, attribute);
            HashMap<String, String>[] arr = lista.toArray();
            Integer n = binaryLineal(arr, attribute, text, type);
            System.out.println("LA N DE LA MITAD: " + n);

            switch (type) {
                case 1:
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    }
                    break;

                case 2:
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    } else if (n < 0) {
                        n *= -1;
                        for (int i = 0; i < n; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase()
                                    .startsWith(text.trim().toLowerCase())) {
                                result.add(arr[i]);
                            }
                        }
                    }
                    break;

                default:
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase()
                                .startsWith(text.trim().toLowerCase())) {
                            result.add(arr[i]);
                        }
                    }
                    break;
            }
        }

        return result;
    }

    private Integer binaryLineal(HashMap<String, String> array[], String attribute, String text, Integer type) {
        System.out.println("[binaryLineal] INICIO: array.length=" + array.length + ", text='" + text + "', attribute='"
                + attribute + "'");
        Integer half = 0;
        if (!(array.length == 0) && !text.isEmpty()) {
            half = array.length / 2;
            int aux = 0;
            char buscado = text.trim().toLowerCase().charAt(0);
            char actual = array[half].get(attribute).toString().toLowerCase().charAt(0);
            if (buscado > actual) {
                aux = 1;
                System.out.println(
                        "[binaryLineal] Se fue a la derecha: '" + buscado + "' > '" + actual + "' (half=" + half + ")");
            } else if (buscado < actual) {
                aux = -1;
                System.out.println("[binaryLineal] Se fue a la izquierda: '" + buscado + "' < '" + actual + "' (half="
                        + half + ")");
                half = half * aux;
                return half;
            } else {
                System.out.println("[binaryLineal] Se quedó en el centro: '" + buscado + "' == '" + actual + "' (half="
                        + half + ")");
            }
        }
        return half;
    }

    public boolean deleteById(int id) {
        try {
            Linkendlist<Cancion> canciones = this.listAll();
            int index = -1;
            for (int i = 0; i < canciones.getLength(); i++) {
                Cancion c = canciones.get(i);
                if (c.getId() == id) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                canciones.delete(index);
                persistAll(canciones);
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void persistAll(Linkendlist<Cancion> canciones) throws Exception {
        java.lang.reflect.Method saveFileMethod = this.getClass().getSuperclass().getDeclaredMethod("saveFIle",
                String.class);
        saveFileMethod.setAccessible(true);
        saveFileMethod.invoke(this, gson().toJson(canciones.toArray()));
    }

    private com.google.gson.Gson gson() {
        try {
            java.lang.reflect.Field gField = this.getClass().getSuperclass().getDeclaredField("g");
            gField.setAccessible(true);
            return (com.google.gson.Gson) gField.get(this);
        } catch (Exception e) {
            return new com.google.gson.Gson();
        }
    }
}
