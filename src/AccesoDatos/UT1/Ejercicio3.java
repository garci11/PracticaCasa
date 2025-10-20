package AccesoDatos.UT1;

//Cree una carpeta llamada pruebas.
//Dentro, cree 3 archivos vacíos.
//Liste su contenido.
//Mueva uno de los archivos a otra carpeta.
//Finalmente, borre todo el árbol de directorios usando Files.walk().

import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Ejercicio3 {

    private static final Path CARPETA_PRUEBAS = Paths.get("pruebas");
    private static final Path SUBCARPETA_DESTINO = Paths.get("destino");
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            System.out.println("1) Crear carpeta 'pruebas' con 3 archivos");
            System.out.println("2) Listar contenido de 'pruebas'");
            System.out.println("3) Mover un archivo a la carpeta 'destino'");
            System.out.println("4) Borrar todas las carpetas y archivos creados");
            System.out.println("5) Salir");
            System.out.print("> ");
            String opcion = sc.nextLine();
            System.out.println();

            switch (opcion) {
                case "1":
                    crearEstructura();
                    break;
                case "2":
                    listarContenido();
                    break;
                case "3":
                    moverArchivo();
                    break;
                case "4":
                    borrarTodo();
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

    //  Crear la carpeta "pruebas" con tres archivos
    private static void crearEstructura() {
        try {
            if (!Files.exists(CARPETA_PRUEBAS)) {
                Files.createDirectory(CARPETA_PRUEBAS);
                System.out.println("Carpeta 'pruebas' creada.");
            }
            for (int i = 1; i <= 3; i++) {
                Path archivo = CARPETA_PRUEBAS.resolve("archivo" + i + ".txt");
                if (!Files.exists(archivo)) {
                    Files.createFile(archivo);
                    System.out.println("Archivo creado: " + archivo.getFileName());
                }
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al crear estructura: " + e.getMessage() + "\n");
        }
    }

    // Listar el contenido sin forEach ni Streams
    private static void listarContenido() {
        if (!Files.exists(CARPETA_PRUEBAS)) {
            System.out.println("La carpeta 'pruebas' no existe.\n");
            return;
        }
        DirectoryStream<Path> directorio = null;
        try {
            directorio = Files.newDirectoryStream(CARPETA_PRUEBAS);
            System.out.println("Contenido de 'pruebas':");
            for (Path p : directorio) {
                System.out.println(" - " + p.getFileName());
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al listar: " + e.getMessage());
        } finally {
            if (directorio != null) {
                try {
                    directorio.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar DirectoryStream: " + e.getMessage());
                }
            }
        }
    }

    // Mover un archivo de "pruebas" a "destino"
    private static void moverArchivo() {
        try {
            if (!Files.exists(CARPETA_PRUEBAS)) {
                System.out.println("La carpeta 'pruebas' no existe.\n");
                return;
            }
            if (!Files.exists(SUBCARPETA_DESTINO)) {
                Files.createDirectory(SUBCARPETA_DESTINO);
                System.out.println("Carpeta 'destino' creada.\n");
            }
            System.out.print("Nombre del archivo a mover (ej: archivo1.txt): ");
            String nombreArchivo = sc.nextLine().trim();

            Path origen = CARPETA_PRUEBAS.resolve(nombreArchivo);
            Path destino = SUBCARPETA_DESTINO.resolve(nombreArchivo);

            if (!Files.exists(origen)) {
                System.out.println("El archivo no existe en 'pruebas'.\n");
                return;
            }
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo movido correctamente.\n");
        } catch (IOException e) {
            System.err.println("Error al mover archivo: " + e.getMessage());
        }
    }

    // Borrar todas las carpetas y archivos creados (recursivo, sin forEach)
    private static void borrarTodo() {
        try {
            if (Files.exists(CARPETA_PRUEBAS)) {
                borrarRecursivo(CARPETA_PRUEBAS);
                System.out.println("Carpeta 'pruebas' eliminada.");
            }
            if (Files.exists(SUBCARPETA_DESTINO)) {
                borrarRecursivo(SUBCARPETA_DESTINO);
                System.out.println("Carpeta 'destino' eliminada.\n");
            }
        } catch (IOException e) {
            System.err.println("Error al borrar: " + e.getMessage());
        }
    }
    // Borrado recursivo clásico sin forEach ni Stream
    private static void borrarRecursivo(Path ruta) throws IOException {
        if (Files.isDirectory(ruta)) {
            DirectoryStream<Path> contenido = null;
            try {
                contenido = Files.newDirectoryStream(ruta);
                for (Path hijo : contenido) {
                    borrarRecursivo(hijo); // llamada recursiva
                }
            } finally {
                if (contenido != null) contenido.close();
            }
        }
        Files.deleteIfExists(ruta);
    }
}
