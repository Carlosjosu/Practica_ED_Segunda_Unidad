package com.unl.estrdts.Practica3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LeerData {
    private static final String filePath = "data.txt";

    public Integer[] extractData() {
        Integer totalNumbers = countNumbers();
        if (totalNumbers == 0) {
            System.out.println("El archivo está vacío o no contiene números válidos.");
            return new Integer[0];
        }

        Integer[] numbers = new Integer[totalNumbers];
        Integer index = 0;

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    String[] parts = line.split(",");
                    for (String part : parts) {
                        numbers[index++] = Integer.parseInt(part.trim());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Línea o parte no válida ignorada: " + line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + filePath);
        }

        return numbers;
    }

    private Integer countNumbers() {
        Integer count = 0;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                count += parts.length;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + filePath);
        }
        return count;
    }

    public void processFile() {
        Integer[] numbers = extractData();

        if (numbers.length == 0) {
            System.out.println("No se encontraron números válidos en el archivo.");
            return;
        }

        System.out.println("Números encontrados en el archivo:");
        for (Integer number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();
    }
}