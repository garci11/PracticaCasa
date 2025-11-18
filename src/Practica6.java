import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Practica6 {

    /**
     * Filtra logs por nivel de severidad
     * Formato: [TIMESTAMP] LEVEL mensaje (IP: X.X.X.X)
     */
    public static List<String> filtrarLogsPorNivel(String archivo, String nivel) {
        List<String> logsFiltrados = new ArrayList<>();
        try {
            List<String> todasLineas = Files.readAllLines(Path.of(archivo));

            for (String linea : todasLineas) {
                // Buscar el nivel en el formato [TIMESTAMP] LEVEL
                if (linea.matches(".*\\] " + nivel + " .*")) {
                    logsFiltrados.add(linea);
                }
            }

        } catch (IOException e) {
            System.err.println("Error leyendo logs: " + e.getMessage());
        }
        return logsFiltrados;
    }

    /**
     * Cuenta errores por hora del día
     */
    public static Map<String, Integer> contarErroresPorHora(List<String> logs) {
        Map<String, Integer> erroresPorHora = new HashMap<>();

        for (String log : logs) {
            // Extraer hora del timestamp [2024-01-15 14:30:25]
            if (log.matches("\\[.*\\].*")) {
                try {
                    String timestamp = log.substring(1, log.indexOf(']'));
                    String hora = timestamp.split(" ")[1].substring(0, 2); // Obtener HH

                    // Contar solo si es ERROR
                    if (log.contains(" ERROR ")) {
                        erroresPorHora.put(hora, erroresPorHora.getOrDefault(hora, 0) + 1);
                    }

                } catch (Exception e) {
                    // Ignorar líneas con formato incorrecto
                }
            }
        }
        return erroresPorHora;
    }

    /**
     * Extrae todas las IPs únicas de los logs
     */
    public static Set<String> extraerIPsUnicas(List<String> logs) {
        Set<String> ipsUnicas = new HashSet<>();

        // Patrón para IPs (simplificado)
        String ipPattern = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";

        for (String log : logs) {
            // Buscar IPs en el log
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(ipPattern);
            java.util.regex.Matcher matcher = pattern.matcher(log);

            while (matcher.find()) {
                ipsUnicas.add(matcher.group());
            }
        }
        return ipsUnicas;
    }

    /**
     * Genera reporte resumen de logs
     */
    public static void generarReporte(String archivo) {
        List<String> todosLogs = new ArrayList<>();
        try {
            todosLogs = Files.readAllLines(Path.of(archivo));
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
            return;
        }

        System.out.println("=== REPORTE DE LOGS ===");
        System.out.println("Total líneas: " + todosLogs.size());

        // Contar por nivel
        long errors = todosLogs.stream().filter(l -> l.contains(" ERROR ")).count();
        long warnings = todosLogs.stream().filter(l -> l.contains(" WARNING ")).count();
        long info = todosLogs.stream().filter(l -> l.contains(" INFO ")).count();

        System.out.printf("ERRORS: %d, WARNINGS: %d, INFO: %d\n", errors, warnings, info);

        System.out.println("\n=== ERRORES POR HORA ===");
        Map<String, Integer> erroresHora = contarErroresPorHora(todosLogs);
        erroresHora.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.printf("Hora %s: %d errores\n", e.getKey(), e.getValue()));

        System.out.println("\n=== IPS ÚNICAS ===");
        Set<String> ips = extraerIPsUnicas(todosLogs);
        System.out.println("Total IPs únicas: " + ips.size());
        ips.forEach(System.out::println);
    }

    public static void main(String[] args) {
        String archivoLogs = "servidor.log";

        System.out.println("=== LOGS DE ERROR ===");
        List<String> errores = filtrarLogsPorNivel(archivoLogs, "ERROR");
        errores.forEach(System.out::println);

        generarReporte(archivoLogs);
    }
}

//Calcular promedio por estudiante
//
//Encontrar mejor promedio
//
//Exportar estudiantes aprobados