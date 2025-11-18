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
            // Inicializamos con NaN (Not a Number) como valor por defecto
            double[] resultado = new double[]{Double.NaN, Double.NaN};

            // VALIDACIONES (condiciones que devuelven NaN):
            // 1. Archivo nulo
            // 2. Archivo vacío
            // 3. Con cabecera pero solo tiene la cabecera
            // 4. Número de columna negativo
            if (file == null || file.isEmpty() ||
                    (heading && file.size() <= 1) ||
                    numeroColumna < 0) {
                return resultado;
            }

            // Determinamos el índice de inicio (igual que en head())
            int inicio;
            if (heading) {
                inicio = 1;  // Saltar cabecera
            } else {
                inicio = 0;  // Incluir todas las líneas
            }

            // Inicializamos valores extremos
            double min = Double.MAX_VALUE;  // Valor máximo posible para double
            double max = Double.MIN_VALUE;  // Valor mínimo posible para double
            boolean datosValidos = false;   // Flag para verificar si encontramos al menos un valor válido

            // Procesamos cada línea del archivo
            for (int i = inicio; i < file.size(); i++) {
                String linea = file.get(i);
                // Dividimos la línea por comas (formato CSV)
                String[] columnas = linea.split(",");

                // Verificamos que la columna solicitada existe en esta línea
                if (numeroColumna < columnas.length) {
                    try {
                        // Intentamos convertir el valor a double
                        // trim() elimina espacios en blanco al inicio y final
                        double valor = Double.parseDouble(columnas[numeroColumna].trim());

                        // Actualizamos mínimo y máximo
                        min = Math.min(min, valor);
                        max = Math.max(max, valor);
                        datosValidos = true;  // Marcamos que encontramos al menos un valor válido

                    } catch (NumberFormatException e) {
                        // Si la conversión falla, ignoramos esta línea
                        // No hacemos nada, simplemente continuamos con la siguiente línea
                    }
                }
            }

            // Solo devolvemos valores reales si encontramos al menos un dato válido
            if (datosValidos) {
                resultado[0] = min;
                resultado[1] = max;
            }

            return resultado;
        }

    /**
     * Método main para demostrar el uso de las funciones
     */
    public static void main(String[] args) {
        // Ejemplo de uso
        Path path = Path.of("datos_ajedrez.csv");
        ArrayList<String> archivo = leerCSV();

        System.out.println("=== HEAD (3 líneas, con cabecera) ===");
        head(3, archivo, true);

        System.out.println("\n=== MIN/MAX Columna 2 ===");
        double[] minMax = minMaxCol(2, archivo, true);
        System.out.println("Mínimo: " + minMax[0] + ", Máximo: " + minMax[1]);
    }
}