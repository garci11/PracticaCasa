package AccesoDatos.UT1;
// quijote

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Ejercicio6 {

    // Ruta del archivo (puedes cambiarla según dónde tengas tu quijote.txt)
    private static final Path ARCHIVO = Paths.get("quijote.txt");

    public static void main(String[] args) {
        System.out.println("=== Análisis del Quijote ===\n");

        if (!Files.exists(ARCHIVO)) {
            System.err.println(" El archivo 'quijote.txt' no existe en la carpeta del proyecto.");
            return;
        }

        BufferedReader br = null;
        int totalPalabras = 0;
        int totalLineas = 0;
        Map<String, Integer> contadorPalabras = new HashMap<>();

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(ARCHIVO.toFile()), StandardCharsets.UTF_8));

            String linea;
            while ((linea = br.readLine()) != null) {
                totalLineas++;

                // Eliminamos signos de puntuación y pasamos a minúsculas
                linea = linea.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9 ]", " ").toLowerCase();

                // Separamos por espacios
                String[] palabras = linea.trim().split("\\s+");

                for (String palabra : palabras) {
                    if (!palabra.isEmpty()) {
                        totalPalabras++;
                        contadorPalabras.put(palabra, contadorPalabras.getOrDefault(palabra, 0) + 1);
                    }
                }
            }

            System.out.println("Líneas analizadas: " + totalLineas);
            System.out.println("Total de palabras: " + totalPalabras);

            double media = (totalLineas > 0) ? (double) totalPalabras / totalLineas : 0;
            System.out.printf("📏 Media de palabras por línea: %.2f%n", media);

            // Buscar la palabra más repetida
            String masRepetida = null;
            int maxFrecuencia = 0;

            for (Map.Entry<String, Integer> entry : contadorPalabras.entrySet()) {
                if (entry.getValue() > maxFrecuencia) {
                    masRepetida = entry.getKey();
                    maxFrecuencia = entry.getValue();
                }
            }

            System.out.println("Palabra más repetida: '" + masRepetida + "' (" + maxFrecuencia + " veces)");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                System.err.println("⚠ Error al cerrar el archivo: " + e.getMessage());
            }
        }

        System.out.println("\nAnálisis completado.");
    }
}

