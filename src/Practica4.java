import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Practica4 {

    /**
     * Lee el archivo CSV completo
     */
    public static List<String> leerArchivoCompleto(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        try {
            // Files.readAllLines lee todo el archivo y devuelve List<String>
            lineas = Files.readAllLines(Path.of(nombreArchivo));
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
        }
        return lineas;
    }

    /**
     * Calcula las ventas totales por mes
     * Formato esperado: fecha,producto,cantidad,precio
     */
    public static Map<String, Double> calcularVentasPorMes(List<String> ventas, boolean tieneCabecera) {
        Map<String, Double> ventasPorMes = new HashMap<>();

        int inicio;
        if (tieneCabecera) {
            inicio = 1; // Saltar cabecera
        } else {
            inicio = 0; // Empezar desde primera línea
        }

        for (int i = inicio; i < ventas.size(); i++) {
            String linea = ventas.get(i);
            String[] campos = linea.split(",");

            if (campos.length >= 4) {
                try {
                    // Extraer mes de la fecha (formato: YYYY-MM-DD)
                    String fecha = campos[0];
                    String mes = fecha.substring(0, 7); // Obtener YYYY-MM

                    int cantidad = Integer.parseInt(campos[2].trim());
                    double precio = Double.parseDouble(campos[3].trim());
                    double totalVenta = cantidad * precio;

                    // Sumar al mes correspondiente
                    ventasPorMes.put(mes, ventasPorMes.getOrDefault(mes, 0.0) + totalVenta);

                } catch (NumberFormatException e) {
                    System.err.println("Error en formato numérico línea " + (i+1));
                }
            }
        }
        return ventasPorMes;
    }

    /**
     * Encuentra el producto más vendido por cantidad
     */
    public static String encontrarProductoMasVendido(List<String> ventas, boolean tieneCabecera) {
        Map<String, Integer> ventasPorProducto = new HashMap<>();

        int inicio;
        if (tieneCabecera) {
            inicio = 1;
        } else {
            inicio = 0;
        }

        for (int i = inicio; i < ventas.size(); i++) {
            String linea = ventas.get(i);
            String[] campos = linea.split(",");

            if (campos.length >= 3) {
                try {
                    String producto = campos[1].trim();
                    int cantidad = Integer.parseInt(campos[2].trim());

                    ventasPorProducto.put(producto,
                            ventasPorProducto.getOrDefault(producto, 0) + cantidad);

                } catch (NumberFormatException e) {
                    System.err.println("Error en cantidad línea " + (i+1));
                }
            }
        }

        // Encontrar producto con máxima cantidad vendida
        return ventasPorProducto.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No hay datos");
    }

    public static void main(String[] args) {
        List<String> ventas = leerArchivoCompleto("ventas.csv");

        System.out.println("=== VENTAS POR MES ===");
        Map<String, Double> ventasMensuales = calcularVentasPorMes(ventas, true);
        ventasMensuales.forEach((mes, total) ->
                System.out.printf("%s: %.2f€\n", mes, total));

        System.out.println("\n=== PRODUCTO MÁS VENDIDO ===");
        String productoTop = encontrarProductoMasVendido(ventas, true);
        System.out.println("Producto más vendido: " + productoTop);
    }
}

//Leer archivo ventas.csv
//
//Calcular venta total por mes
//
//Encontrar el producto más vendido