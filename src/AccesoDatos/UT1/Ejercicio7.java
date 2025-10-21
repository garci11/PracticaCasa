package AccesoDatos.UT1;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.Comparator;

public class Ejercicio7 {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== GESTOR DE ARCHIVOS Y DIRECTORIOS (CRUD sin try-with-resources) ===\n");

        boolean salir = false;
        while (!salir) {
            System.out.println("Seleccione una opción:");
            System.out.println("1) Crear archivo o directorio");
            System.out.println("2) Listar contenido de un directorio");
            System.out.println("3) Mover o renombrar archivo/directorio");
            System.out.println("4) Borrar archivo o directorio");
            System.out.println("5) Salir");
            System.out.print("> ");
            String opcion = sc.nextLine().trim();
            System.out.println();

            switch (opcion) {
                case "1":
                    crear();
                    break;
                case "2":
                    listar();
                    break;
                case "3":
                    moverORenombrar();
                    break;
                case "4":
                    borrar();
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

    //  CREAR archivo o directorio
    private static void crear() {
        try {
            System.out.print("¿Qué quieres crear? (archivo/directorio): ");
            String tipo = sc.nextLine().trim().toLowerCase();

            System.out.print("Introduce la ruta o nombre: ");
            String nombre = sc.nextLine().trim();
            Path ruta = Paths.get(nombre);

            if (tipo.equals("archivo")) {
                if (!Files.exists(ruta)) {
                    Files.createFile(ruta);
                    System.out.println("Archivo creado: " + ruta.getFileName());
                    escribirContenido(ruta);
                } else {
                    System.out.println("El archivo ya existe.");
                }
            } else if (tipo.equals("directorio")) {
                if (!Files.exists(ruta)) {
                    Files.createDirectories(ruta);
                    System.out.println("Directorio creado: " + ruta);
                } else {
                    System.out.println("El directorio ya existe.");
                }
            } else {
                System.out.println("Tipo no válido. Usa 'archivo' o 'directorio'.");
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al crear: " + e.getMessage() + "\n");
        }
    }

    // Método auxiliar: escribir contenido en un archivo recién creado
    private static void escribirContenido(Path archivo) {
        BufferedWriter bw = null;
        try {
            System.out.println("¿Deseas escribir algo dentro del archivo? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (!resp.equals("s")) return;

            bw = Files.newBufferedWriter(archivo, StandardOpenOption.APPEND);
            System.out.println("Escribe las líneas (vacío para terminar):");
            while (true) {
                String linea = sc.nextLine();
                if (linea.isEmpty()) break;
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Contenido guardado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir: " + e.getMessage());
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar archivo: " + e.getMessage());
            }
        }
    }

    //  LEER (listar contenido de un directorio)
    private static void listar() {
        try {
            System.out.print("Introduce el nombre o ruta del directorio: ");
            String nombre = sc.nextLine().trim();
            Path carpeta = Paths.get(nombre);

            if (!Files.exists(carpeta) || !Files.isDirectory(carpeta)) {
                System.out.println("No existe el directorio especificado.\n");
                return;
            }

            Stream<Path> lista = Files.list(carpeta);
            System.out.println("Contenido de '" + carpeta + "':");
            lista.forEach(p -> {
                try {
                    if (Files.isDirectory(p))
                        System.out.println("[DIR]  " + p.getFileName());
                    else
                        System.out.println("[FILE] " + p.getFileName() + " (" + Files.size(p) + " bytes)");
                } catch (IOException e) {
                    System.err.println("Error al obtener info de: " + p);
                }
            });
            lista.close();
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error al listar: " + e.getMessage() + "\n");
        }
    }

    // UPDATE (mover o renombrar)
    private static void moverORenombrar() {
        try {
            System.out.print("Introduce la ruta actual del archivo o directorio: ");
            Path origen = Paths.get(sc.nextLine().trim());
            if (!Files.exists(origen)) {
                System.out.println("El elemento no existe.\n");
                return;
            }

            System.out.print("Introduce la nueva ruta o nombre: ");
            Path destino = Paths.get(sc.nextLine().trim());

            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Elemento movido o renombrado correctamente.\n");

        } catch (IOException e) {
            System.err.println("Error al mover/renombrar: " + e.getMessage() + "\n");
        }
    }

    //  DELETE (borrar archivo o directorio)
    private static void borrar() {
        try {
            System.out.print("Introduce la ruta o nombre a borrar: ");
            Path ruta = Paths.get(sc.nextLine().trim());

            if (!Files.exists(ruta)) {
                System.out.println("El archivo/directorio no existe.\n");
                return;
            }

            if (Files.isDirectory(ruta)) {
                borrarRecursivo(ruta);
                System.out.println("Directorio borrado correctamente.\n");
            } else {
                Files.deleteIfExists(ruta);
                System.out.println("Archivo borrado correctamente.\n");
            }
        } catch (IOException e) {
            System.err.println("Error al borrar: " + e.getMessage() + "\n");
        }
    }

    // Método auxiliar para borrar directorios no vacíos
    private static void borrarRecursivo(Path ruta) throws IOException {
        Stream<Path> walk = null;
        try {
            walk = Files.walk(ruta);
            walk.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    System.err.println("No se pudo borrar: " + p + " -> " + e.getMessage());
                }
            });
        } finally {
            if (walk != null) walk.close();
        }
    }
    private static void borrarRecursivoProfundo(Path ruta) {
        Stream<Path> recorrido = null;
        try {
            // Recorremos todo el árbol del directorio
            recorrido = Files.walk(ruta);

            // Ordenamos de forma inversa: primero los archivos, luego los directorios
            recorrido.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                            System.out.println("Borrado: " + p);
                        } catch (IOException e) {
                            System.err.println("No se pudo borrar " + p + " -> " + e.getMessage());
                        }
                    });

            System.out.println("✅ Directorio '" + ruta.getFileName() + "' eliminado completamente.\n");
        } catch (IOException e) {
            System.err.println("Error al recorrer el árbol de directorios: " + e.getMessage());
        } finally {
            if (recorrido != null) recorrido.close(); // Cerramos el stream manualmente
        }
    }
// ejemplo de uso
// public static void main(String[] args) {
//    Path carpeta = Paths.get("miCarpetaGrande");
//
//    if (Files.exists(carpeta)) {
//        borrarRecursivoProfundo(carpeta);
//    } else {
//        System.out.println("No existe el directorio especificado.");
//    }
//}
private static void vaciarDirectorio(Path ruta) {
    Stream<Path> recorrido = null;
    try {
        recorrido = Files.walk(ruta)
                .filter(p -> !p.equals(ruta)) // excluye la carpeta raíz
                .sorted(Comparator.reverseOrder());

        recorrido.forEach(p -> {
            try {
                Files.deleteIfExists(p);
                System.out.println("Borrado: " + p);
            } catch (IOException e) {
                System.err.println("Error al borrar: " + p + " -> " + e.getMessage());
            }
        });

        System.out.println("✅ Contenido del directorio '" + ruta.getFileName() + "' eliminado.\n");
    } catch (IOException e) {
        System.err.println("Error al vaciar el directorio: " + e.getMessage());
    } finally {
        if (recorrido != null) recorrido.close();
    }
}

}
