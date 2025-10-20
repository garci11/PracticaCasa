package AccesoDatos.UT1;

//Crea un programa que abra un archivo inexistente.
//Maneja la excepción y muestra un mensaje de error personalizado.

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Ejercicio5 {
    private static final Path ARCHIVO = Paths.get("archivo_inexistente.txt");
    public static void main(String[] args) {

        //  Ejemplo: intentar leer un archivo inexistente
        leerArchivoInexistente();

        //  Ejemplo: manejo de múltiples excepciones
        manejarMultiplesExcepciones();

        //  Ejemplo: bloque finally para cerrar recursos
        cerrarRecursosManual();

        System.out.println("\nPrograma finalizado correctamente.");
    }

    //  Intentar leer un archivo que no existe (IOException)
    private static void leerArchivoInexistente() {
        BufferedReader br = null;
        try {
            System.out.println("Intentando leer '" + ARCHIVO.getFileName() + "' ...");
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(ARCHIVO.toFile()), StandardCharsets.UTF_8));

            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: el archivo '" + ARCHIVO.getFileName() + "' no existe.");
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                    System.out.println("✔ Recurso cerrado correctamente (BufferedReader).");
                }
            } catch (IOException e) {
                System.err.println("⚠ Error al cerrar el archivo: " + e.getMessage());
            }
        }
        System.out.println();
    }

    // Manejo de varias excepciones en un mismo bloque
    private static void manejarMultiplesExcepciones() {
        System.out.println("Probando manejo de múltiples excepciones...");

        try {
            int[] numeros = {10, 20, 30};
            System.out.println("Elemento 5: " + numeros[5]); // Error: índice inválido
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(" Error: intentaste acceder a una posición fuera del arreglo.");
        } catch (Exception e) {
            System.err.println("⚠ Se produjo un error inesperado: " + e.getMessage());
        }

        System.out.println("El programa sigue ejecutándose tras la excepción.\n");
    }

    // Ejemplo con bloque finally para cerrar manualmente un recurso abierto
    private static void cerrarRecursosManual() {
        BufferedWriter bw = null;
        try {
            System.out.println("Creando archivo de prueba 'salida.txt'...");
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("salida.txt"), StandardCharsets.UTF_8));

            bw.write("Este archivo fue creado correctamente.\n");
            bw.write("Demostración del uso de finally para cerrar recursos.\n");

            System.out.println("✔ Archivo 'salida.txt' escrito sin errores.");

        } catch (IOException e) {
            System.err.println(" Error al escribir el archivo: " + e.getMessage());
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    System.out.println("✔ Recurso cerrado correctamente (BufferedWriter).");
                }
            } catch (IOException e) {
                System.err.println("⚠ Error al cerrar el archivo: " + e.getMessage());
            }
        }
        System.out.println();
    }
}
