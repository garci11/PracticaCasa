package AccesoDatos.UT1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Practica1 {

    /**
     * Método 1: Lee todo el fichero CSV línea a línea
     * path Ruta del archivo CSV
     * ArrayList con todas las líneas del archivo
     */
    private static final Path ARCHIVO = Paths.get("datos_ajedrez.csv");

    private static ArrayList<String> leerCSV() {
        ArrayList<String> lineas = new ArrayList<>();
        BufferedReader br = null;
        String linea = "";

        try {
            br = new BufferedReader(new FileReader(ARCHIVO.toFile())); // Mejor usar ARCHIVO

            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();

        } finally {

            // Cerrar en el bloque finally
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar: " + e.getMessage());
                }
            }
        }
        return lineas;
    }


    /**
     * Método 2: Imprime las primeras n líneas del CSV
     * lineas Número de líneas a imprimir
     * file ArrayList con el contenido del archivo
     * heading Si es true, considera que hay cabecera
     */

    public static void head(int lineas, ArrayList<String> file, boolean heading) {

        if (file == null || file.isEmpty()) {
            System.out.println("El archivo está vacío");
            return;
        }

        int inicio = 0;

        // Si hay heading, mostrar la cabecera primero
        if (heading) {
            System.out.println("CABECERA: " + file.get(0));
            inicio = 1; // Empezamos desde la línea 1 (segunda línea)
        }

        // Calcular hasta dónde imprimir
        // Math.min evita salir del rango si pedimos más líneas de las que hay
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        //  int limite = Math.min(inicio + lineas, file.size());

        // Imprimir las líneas
        for (int i = inicio; i < n; i++) {
            System.out.println(file.get(i));
        }
    }


        /**
         * Método 3: Calcula el mínimo y máximo de una columna
         * numeroColumna Índice de la columna
         *  file ArrayList con el contenido del archivo
         *  heading Si es true, considera que hay cabecera
         *  Array de double {mínimo, máximo} o {NaN, NaN} si hay error
         */
    public static double[] minMaxCol(int numeroColumna, ArrayList<String> file, boolean heading) {
        // Array de retorno: {mínimo, máximo}
        double[] resultado = {Double.NaN, Double.NaN};

        if (file == null || file.isEmpty()) {
            System.out.println("El archivo está vacio");
            return resultado;
        }

        int inicio;
        if (heading) {
            inicio = 1;
        }else{
            inicio = 0;
        }

        // Variables para rastrear min y max
        double minimo = Double.MAX_VALUE;
        double maximo = Double.MIN_VALUE;
        boolean hayValoresValidos = false;

        for (int i = inicio; i < file.size(); i++) {
            String linea = file.get(i);

            // Dividimos la línea por comas
            String[] columnas = linea.split(",");


            try {
                // Intentamos convertir el valor a double
                double valor = Double.parseDouble(columnas[numeroColumna].trim());

                // Actualizamos mínimo y máximo
                if (valor < minimo) {
                    minimo = valor;
                }
                if (valor > maximo) {
                    maximo = valor;
                }

                hayValoresValidos = true;

            } catch (NumberFormatException e) {
                // Si no se puede convertir a número, lo ignoramos
                continue;
            }
        }

        // Si encontramos valores válidos, los devolvemos
        if (hayValoresValidos) {
            resultado[0] = minimo;
            resultado[1] = maximo;
        }

        return resultado;
    }

    /**
     * Método main para probar el código
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DEL CÓDIGO CORREGIDO ===\n");

        // 1. Leer el CSV completo
        ArrayList<String> contenido = leerCSV();
        System.out.println("\n✓ Archivo leído: " + contenido.size() + " líneas\n");

        // 2. Mostrar las primeras 3 líneas CON heading
        System.out.println("--- HEAD con heading=true (3 líneas) ---");
        head(3, contenido, true);

        // 3. Mostrar las primeras 3 líneas SIN heading
        System.out.println("\n--- HEAD con heading=false (3 líneas) ---");
        head(3, contenido, false);

        // 4. Calcular min y max de la columna 1
        System.out.println("\n--- MIN-MAX de columna 1 ---");
        ArrayList<Double> resultado = minMaxCol(1, contenido, true);
        if (resultado.size() == 2) {
            System.out.println("Mínimo: " + resultado.get(0));
            System.out.println("Máximo: " + resultado.get(1));
        }
    }
}