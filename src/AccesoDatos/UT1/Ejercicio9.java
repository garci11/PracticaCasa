package AccesoDatos.UT1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Ejercicio9 {

    private static final Path ARCHIVO_CSV = Paths.get("alumnos.csv");
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== GESTOR CRUD DE ARCHIVO CSV (sin try-with-resources) ===\n");

        boolean salir = false;
        while (!salir) {
            System.out.println("1) Crear archivo CSV");
            System.out.println("2) Leer y mostrar todos los registros");
            System.out.println("3) Añadir un nuevo alumno");
            System.out.println("4) Modificar alumno por ID");
            System.out.println("5) Eliminar alumno por ID");
            System.out.println("6) Buscar alumno por nombre");
            System.out.println("7) Exportar alumnos con nota >= 8 a otro CSV");
            System.out.println("8) Salir");
            System.out.print("> ");
            String opcion = sc.nextLine();
            System.out.println();

            switch (opcion) {
                case "1": crearCSV(); break;
                case "2": leerCSV(); break;
                case "3": anadirAlumno(); break;
                case "4": modificarAlumno(); break;
                case "5": eliminarAlumno(); break;
                case "6": buscarPorNombre(); break;
                case "7": exportarSobresalientes(); break;
                case "8": salir = true; break;
                default: System.out.println("Opción no válida.\n");
            }
        }

        System.out.println("Programa finalizado.");
    }

    // Crear CSV inicial con encabezado y algunos datos
    private static void crearCSV() {
        BufferedWriter bw = null;
        try {
            bw = Files.newBufferedWriter(ARCHIVO_CSV, StandardCharsets.UTF_8);
            bw.write("id,nombre,edad,curso,nota");
            bw.newLine();
            bw.write("1,Ana,20,Informática,8.7");
            bw.newLine();
            bw.write("2,Juan,21,Informática,7.5");
            bw.newLine();
            bw.write("3,Lucía,19,Telecomunicaciones,9.1");
            bw.newLine();
            System.out.println("Archivo CSV creado correctamente.\n");
        } catch (IOException e) {
            System.err.println("Error al crear el archivo CSV: " + e.getMessage());
        } finally {
            try { if (bw != null) bw.close(); } catch (IOException ignored) {}
        }
    }

    //  Leer CSV y mostrarlo en pantalla
    private static void leerCSV() {
        if (!Files.exists(ARCHIVO_CSV)) {
            System.out.println("El archivo CSV no existe.\n");
            return;
        }
        BufferedReader br = null;
        try {
            br = Files.newBufferedReader(ARCHIVO_CSV, StandardCharsets.UTF_8);
            String linea;
            System.out.println("Contenido del archivo CSV:");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al leer el CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException ignored) {}
        }
    }

    //  Añadir un nuevo alumno al final
    private static void anadirAlumno() {
        BufferedWriter bw = null;
        try {
            bw = Files.newBufferedWriter(ARCHIVO_CSV, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            System.out.print("ID: "); String id = sc.nextLine();
            System.out.print("Nombre: "); String nombre = sc.nextLine();
            System.out.print("Edad: "); String edad = sc.nextLine();
            System.out.print("Curso: "); String curso = sc.nextLine();
            System.out.print("Nota: "); String nota = sc.nextLine();

            bw.write(id + "," + nombre + "," + edad + "," + curso + "," + nota);
            bw.newLine();
            System.out.println("Alumno añadido correctamente.\n");
        } catch (IOException e) {
            System.err.println("Error al escribir en el CSV: " + e.getMessage());
        } finally {
            try { if (bw != null) bw.close(); } catch (IOException ignored) {}
        }
    }

    //  Modificar datos de un alumno (por ID)
    private static void modificarAlumno() {
        if (!Files.exists(ARCHIVO_CSV)) {
            System.out.println("El archivo CSV no existe.\n");
            return;
        }

        List<String> lineas = new ArrayList<>();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = Files.newBufferedReader(ARCHIVO_CSV, StandardCharsets.UTF_8);
            String linea;
            System.out.print("ID del alumno a modificar: ");
            String idBuscado = sc.nextLine();
            boolean encontrado = false;

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith(idBuscado + ",")) {
                    encontrado = true;
                    System.out.println("Alumno encontrado: " + linea);
                    System.out.print("Nuevo nombre: "); String nombre = sc.nextLine();
                    System.out.print("Nueva edad: "); String edad = sc.nextLine();
                    System.out.print("Nuevo curso: "); String curso = sc.nextLine();
                    System.out.print("Nueva nota: "); String nota = sc.nextLine();
                    lineas.add(idBuscado + "," + nombre + "," + edad + "," + curso + "," + nota);
                } else {
                    lineas.add(linea);
                }
            }

            if (!encontrado) {
                System.out.println("No se encontró un alumno con ese ID.\n");
                return;
            }

            bw = Files.newBufferedWriter(ARCHIVO_CSV, StandardCharsets.UTF_8);
            for (String l : lineas) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("Alumno modificado correctamente.\n");

        } catch (IOException e) {
            System.err.println("Error al modificar CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); if (bw != null) bw.close(); } catch (IOException ignored) {}
        }
    }

    //  Eliminar alumno por ID
    private static void eliminarAlumno() {
        if (!Files.exists(ARCHIVO_CSV)) {
            System.out.println("El archivo CSV no existe.\n");
            return;
        }

        List<String> lineas = new ArrayList<>();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = Files.newBufferedReader(ARCHIVO_CSV, StandardCharsets.UTF_8);
            String linea;
            System.out.print("ID del alumno a eliminar: ");
            String idBuscado = sc.nextLine();
            boolean eliminado = false;

            while ((linea = br.readLine()) != null) {
                if (!linea.startsWith(idBuscado + ",")) {
                    lineas.add(linea);
                } else {
                    eliminado = true;
                }
            }

            if (!eliminado) {
                System.out.println("No se encontró un alumno con ese ID.\n");
                return;
            }

            bw = Files.newBufferedWriter(ARCHIVO_CSV, StandardCharsets.UTF_8);
            for (String l : lineas) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("Alumno eliminado correctamente.\n");

        } catch (IOException e) {
            System.err.println("Error al eliminar del CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); if (bw != null) bw.close(); } catch (IOException ignored) {}
        }
    }

    //  Buscar alumno por nombre
    private static void buscarPorNombre() {
        if (!Files.exists(ARCHIVO_CSV)) {
            System.out.println("El archivo CSV no existe.\n");
            return;
        }

        BufferedReader br = null;
        try {
            br = Files.newBufferedReader(ARCHIVO_CSV, StandardCharsets.UTF_8);
            System.out.print("Nombre a buscar: ");
            String nombre = sc.nextLine().toLowerCase();

            String linea;
            boolean encontrado = false;
            while ((linea = br.readLine()) != null) {
                if (linea.toLowerCase().contains(nombre)) {
                    System.out.println("Coincidencia: " + linea);
                    encontrado = true;
                }
            }

            if (!encontrado) System.out.println("No se encontró ningún alumno con ese nombre.\n");

        } catch (IOException e) {
            System.err.println("Error al buscar en el CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException ignored) {}
        }
    }

    // Exportar alumnos con nota >= 8
    private static void exportarSobresalientes() {
        if (!Files.exists(ARCHIVO_CSV)) {
            System.out.println("El archivo CSV no existe.\n");
            return;
        }

        BufferedReader br = null;
        BufferedWriter bw = null;
        Path exportado = Paths.get("sobresalientes.csv");
        try {
            br = Files.newBufferedReader(ARCHIVO_CSV, StandardCharsets.UTF_8);
            bw = Files.newBufferedWriter(exportado, StandardCharsets.UTF_8);
            String linea = br.readLine(); // encabezado
            bw.write(linea);
            bw.newLine();

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 5) {
                    double nota = Double.parseDouble(partes[4]);
                    if (nota >= 8.0) {
                        bw.write(linea);
                        bw.newLine();
                    }
                }
            }
            System.out.println("Archivo 'sobresalientes.csv' generado con éxito.\n");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al exportar: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); if (bw != null) bw.close(); } catch (IOException ignored) {}
        }
    }
}
