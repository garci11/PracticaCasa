package AccesoDatos.UT1;

//Cree un archivo binario datos.dat
//Guarde varios tipos de datos (int, double, boolean, String)
//Lea y muestre los datos usando DataInputStream y DataOutputStream.

import java.io.*;
import java.nio.file.*;

public class Ejercicio2 {

    private static final Path ARCHIVO_BINARIO = Paths.get("datos_binarios.dat");

    public static void main(String[] args) {
        escribirDatos();
        leerDatos();
        System.out.println("\nPrograma finalizado.");
    }

    //  Escribir varios tipos de datos primitivos a un archivo binario
    private static void escribirDatos() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(ARCHIVO_BINARIO.toFile()));

            System.out.println("Escribiendo datos en archivo binario...\n");

            dos.writeUTF("Juan Pérez");
            dos.writeInt(25);
            dos.writeDouble(8.75);
            dos.writeBoolean(true);
            System.out.println("Archivo binario '" + ARCHIVO_BINARIO.getFileName() + "' creado correctamente.\n");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo binario: " + e.getMessage());
        } finally {
            try {
                if (dos != null) dos.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el flujo de salida: " + e.getMessage());
            }
        }
    }
    // Leer los datos del archivo binario y mostrarlos en consola
    private static void leerDatos() {
        if (!Files.exists(ARCHIVO_BINARIO)) {
            System.out.println("El archivo binario no existe. Ejecute primero la escritura.\n");
            return;
        }
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(ARCHIVO_BINARIO.toFile()));
            System.out.println("Leyendo datos desde el archivo binario...\n");
            String nombre = dis.readUTF();
            int edad = dis.readInt();
            double nota = dis.readDouble();
            boolean aprobado = dis.readBoolean();
            System.out.println("Contenido leído:");
            System.out.println("Nombre: " + nombre);
            System.out.println("Edad: " + edad);
            System.out.println("Nota: " + nota);
            System.out.println("Aprobado: " + aprobado);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo binario: " + e.getMessage());
        } finally {
            try {
                if (dis != null) dis.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el flujo de entrada: " + e.getMessage());
            }
        }
    }
}