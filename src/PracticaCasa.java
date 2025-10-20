
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class PracticaCasa {

    private static final Path ARCHIVO = Paths.get("datos.txt");
    private static final Path COPIA = Paths.get("copia_datos.txt");
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("1) Crear archivo y escribir nombre, edad y curso");
            System.out.println("2) Leer archivo línea a línea");
            System.out.println("3) Añadir (append) nuevas líneas");
            System.out.println("4) Copiar contenido a otro archivo");
            System.out.println("5) Salir");
            System.out.print("> ");
            String opcion = sc.nextLine();
            System.out.println();

            switch (opcion) {
                case "1":
                    crearYEscribir();
                    break;
                case "2":
                    leerArchivo();
                    break;
                case "3":
                    anadirLineas();
                    break;
                case "4":
                    copiarArchivo();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.\n");
            }
        }

        System.out.println("Programa finalizado.");
    }

    // Crear archivo y escribir (sobrescribe)
    private static void crearYEscribir() {
        BufferedWriter bw = null;
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Edad: ");
            String edad = sc.nextLine();
            System.out.print("Curso: ");
            String curso = sc.nextLine();

            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(ARCHIVO.toFile(), false)));

            bw.write("Nombre: " + nombre);
            bw.newLine();
            bw.write("Edad: " + edad);
            bw.newLine();
            bw.write("Curso: " + curso);
            bw.newLine();

            System.out.println("Archivo '" + ARCHIVO.getFileName() + "' creado correctamente.\n");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }

    //  Leer archivo línea a línea
    private static void leerArchivo() {
        if (!Files.exists(ARCHIVO)) {
            System.out.println("El archivo no existe. Crea primero el archivo (opción 1).\n");
            return;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(ARCHIVO.toFile()), StandardCharsets.UTF_8));

            String linea;
            int n = 1;
            System.out.println("Contenido de '" + ARCHIVO.getFileName() + "':");
            while ((linea = br.readLine()) != null) {
                System.out.println(n + ": " + linea);
                n++;
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el lector: " + e.getMessage());
            }
        }
    }

    // Añadir nuevas líneas
    private static void anadirLineas() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(ARCHIVO.toFile(), true), StandardCharsets.UTF_8));

            System.out.print("¿Cuántas líneas quieres añadir?: ");
            int n = Integer.parseInt(sc.nextLine());

            for (int i = 1; i <= n; i++) {
                System.out.print("Línea " + i + ": ");
                String texto = sc.nextLine();
                bw.write(texto);
                bw.newLine();
            }

            System.out.println("Se añadieron " + n + " líneas al archivo.\n");
        } catch (IOException e) {
            System.err.println("Error al añadir texto: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: se esperaba un número entero.\n");
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }

    // Copiar contenido a otro archivo
    private static void copiarArchivo() {
        if (!Files.exists(ARCHIVO)) {
            System.out.println("El archivo no existe. Crea primero el archivo (opción 1).\n");
            return;
        }

        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(ARCHIVO.toFile())));

            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(COPIA.toFile(), false)));

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }

            System.out.println("Archivo copiado correctamente a '" + COPIA.getFileName() + "'.\n");
        } catch (IOException e) {
            System.err.println("Error al copiar el archivo: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar flujos: " + e.getMessage());
            }
        }
    }
}