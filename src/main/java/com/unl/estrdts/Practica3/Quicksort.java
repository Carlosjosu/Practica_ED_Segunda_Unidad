package com.unl.estrdts.Practica3;

public class Quicksort {

    public static void quickSort(Linkendlist<Integer> vec, int inicio, int fin) throws Exception {
        if (inicio >= fin)
            return;
        int pivote = vec.get(inicio);
        int elemIzq = inicio + 1;
        int elemDer = fin;
        while (elemIzq <= elemDer) {

            while (elemIzq <= fin && vec.get(elemIzq) < pivote) {
                elemIzq++;
            }
            while (elemDer > inicio && vec.get(elemDer) >= pivote) {
                elemDer--;
            }
            if (elemIzq < elemDer) {
                Integer temp = vec.get(elemIzq);
                vec.update(vec.get(elemDer), elemIzq);
                vec.update(temp, elemDer);
            }
        }

        if (elemDer > inicio) {
            Integer temp = vec.get(inicio);
            vec.update(vec.get(elemDer), inicio);
            vec.update(temp, elemDer);
        }
        quickSort(vec, inicio, elemDer - 1);
        quickSort(vec, elemDer + 1, fin);
    }

    public static void main(String[] args) {
        LeerData lector = new LeerData();
        Integer[] datos = lector.extractData();
        if (datos.length == 0) {
            System.out.println("El archivo está vacío o no se pudo leer correctamente");
            return;
        }
        Linkendlist<Integer> datosQuick = new Linkendlist<>();
        Linkendlist<Integer> datosShell = new Linkendlist<>();
        datosQuick.toList(datos);
        datosShell.toList(datos);

        long inicioQuickSort = System.currentTimeMillis();
        try {
            Quicksort.quickSort(datosQuick, 0, datosQuick.getLength() - 1);
        } catch (Exception e) {
            System.out.println("Error en QuickSort: " + e.getMessage());
        }
        long finQuickSort = System.currentTimeMillis();
        System.out.print("El tiempo que demoró el método QuickSort en ordenar fue de: " +
                (finQuickSort - inicioQuickSort) + " milisegundos\n");

        long inicioShellSort = System.currentTimeMillis();
        try {
            ShellSort.shellSort(datosShell);
        } catch (Exception e) {
            System.out.println("Error en ShellSort: " + e.getMessage());
        }
        long finShellSort = System.currentTimeMillis();
        System.out.print("El tiempo que demoró el método ShellSort en ordenar fue de: " +
                (finShellSort - inicioShellSort) + " milisegundos\n");

    }
}
