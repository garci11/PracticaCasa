import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import java.io.File;
import java.util.*;

public class Practica2 {

    private static Document doc;
    private static XPath xpath;

    /**
     * Ejercicio 2.1: Lista las diferentes categorías del documento
     * Devuelve ArrayList con [historia, estadisticas, consejos]
     * @return ArrayList<String> con las categorías únicas
     */
    public static ArrayList<String> listarCategorias() {
        ArrayList<String> categorias = new ArrayList<>();

        try {
            // Buscar todos los elementos "categoria"
            String expresion = "//categoria";
            NodeList nodos = (NodeList) xpath.evaluate(expresion, doc, XPathConstants.NODESET);

            System.out.println("=== LISTADO DE CATEGORÍAS ===");
            System.out.println("Total de elementos 'categoria' encontrados: " + nodos.getLength());

            // Usar un Set para evitar duplicados
            Set<String> categoriasUnicas = new TreeSet<>(); // TreeSet para ordenarlas

            for (int i = 0; i < nodos.getLength(); i++) {
                Element categoria = (Element) nodos.item(i);
                String nombreCategoria = categoria.getTextContent().trim();
                categoriasUnicas.add(nombreCategoria);
            }

            // Convertir Set a ArrayList
            categorias.addAll(categoriasUnicas);

            System.out.println("\nCategorías únicas encontradas:");
            for (String cat : categorias) {
                System.out.println("  - " + cat);
            }
            System.out.println();

        } catch (XPathExpressionException e) {
            System.err.println("❌ Error en la expresión XPath: " + e.getMessage());
        }

        return categorias;
    }

    /**
     * Ejercicio 2.2: Encuentra el año con mayor porcentaje de uso de tarjeta de crédito
     * Filtra para años > 2020
     * @return Año con mayor porcentaje (después de 2020)
     */
    public static int encontrarAnioMayorUso() {
        int anioMayor = -1;
        double porcentajeMayor = -1;

        try {
            System.out.println("=== BÚSQUEDA DE AÑO CON MAYOR USO (> 2020) ===");

            // Buscar todos los elementos que tengan año y porcentaje
            String expresion = "//estadistica";
            NodeList estadisticas = (NodeList) xpath.evaluate(expresion, doc, XPathConstants.NODESET);

            System.out.println("Estadísticas encontradas: " + estadisticas.getLength());
            System.out.println("\nAnalizando datos:");

            for (int i = 0; i < estadisticas.getLength(); i++) {
                Element estadistica = (Element) estadisticas.item(i);

                // Obtener año
                NodeList aniosNodes = estadistica.getElementsByTagName("anio");
                NodeList porcentajesNodes = estadistica.getElementsByTagName("porcentaje");

                if (aniosNodes.getLength() > 0 && porcentajesNodes.getLength() > 0) {
                    try {
                        int anio = Integer.parseInt(aniosNodes.item(0).getTextContent().trim());
                        double porcentaje = Double.parseDouble(porcentajesNodes.item(0).getTextContent().trim());

                        System.out.printf("  Año: %d, Porcentaje: %.2f%%", anio, porcentaje);

                        // Verificar si es mayor a 2020 y si es el mayor porcentaje
                        if (anio > 2020) {
                            if (porcentaje > porcentajeMayor) {
                                porcentajeMayor = porcentaje;
                                anioMayor = anio;
                                System.out.print(" ← NUEVO MÁXIMO");
                            }
                        } else {
                            System.out.print(" (descartado, ≤ 2020)");
                        }

                        System.out.println();

                    } catch (NumberFormatException e) {
                        System.err.println("⚠ Error al parsear números en estadística " + (i + 1));
                    }
                }
            }

            System.out.println("\n✓ Resultado:");
            System.out.println("  Año con mayor uso: " + anioMayor);
            System.out.printf("  Porcentaje: %.2f%%\n\n", porcentajeMayor);

        } catch (XPathExpressionException e) {
            System.err.println("❌ Error en la expresión XPath: " + e.getMessage());
        }

        return anioMayor;
    }

    /**
     * Ejercicio 2.3: Añade un "consejo" tras los que ya están
     * Añade un consejo para recordar pagar y evitar intereses excesivos
     * Asigna el atributo category como el resto (category="general")
     * @param textoConsejo El texto del consejo a añadir
     */
    public static void aniadirConsejo(String textoConsejo) {
        try {
            System.out.println("=== AÑADIENDO NUEVO CONSEJO ===");

            // Buscar el nodo "consejos" donde añadir el nuevo consejo
            String expresion = "//consejos";
            Node nodosConsejos = (Node) xpath.evaluate(expresion, doc, XPathConstants.NODE);

            if (nodosConsejos == null) {
                System.err.println("❌ No se encontró el elemento 'consejos'");
                return;
            }

            // Crear el nuevo elemento "consejo"
            Element nuevoConsejo = doc.createElement("consejo");
            nuevoConsejo.setAttribute("category", "general");
            nuevoConsejo.setTextContent(textoConsejo);

            // Añadir al nodo "consejos"
            nodosConsejos.appendChild(nuevoConsejo);

            System.out.println("✓ Consejo añadido correctamente:");
            System.out.println("  Texto: " + textoConsejo);
            System.out.println("  Atributo: category=\"general\"\n");

        } catch (XPathExpressionException e) {
            System.err.println("❌ Error en la expresión XPath: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 2.4: Reescribe el archivo en un archivo llamado articulo_mod.xml
     */
    public static void reescribirArchivo() {
        try {
            System.out.println("=== GUARDANDO ARCHIVO MODIFICADO ===");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Configurar formato
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Crear el origen y destino
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("articulo_mod.xml"));

            // Realizar la transformación
            transformer.transform(source, result);

            System.out.println("✓ Archivo guardado como 'articulo_mod.xml'");
            System.out.println("  Ruta: " + new File("articulo_mod.xml").getAbsolutePath());
            System.out.println();

        } catch (TransformerException e) {
            System.err.println("❌ Error al guardar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método principal
     */
    public static void main(String[] args) {
        try {
            System.out.println("╔═══════════════════════════════════════════════╗");
            System.out.println("║   EJERCICIO 2: PROCESAMIENTO XML ARTÍCULO    ║");
            System.out.println("╚═══════════════════════════════════════════════╝\n");

            // Crear archivo XML de ejemplo si no existe
            crearArchivoXMLEjemplo();

            // Inicializar el documento
            inicializarDocumento();

            // 1. Listar categorías (1.25 puntos)
            System.out.println("1. LISTANDO CATEGORÍAS");
            System.out.println("─────────────────────────────────────");
            ArrayList<String> categorias = listarCategorias();

            // 2. Encontrar año con mayor porcentaje > 2020 (1.25 puntos)
            System.out.println("2. BUSCANDO AÑO CON MAYOR USO (> 2020)");
            System.out.println("─────────────────────────────────────");
            int anio = encontrarAnioMayorUso();

            // 3. Añadir consejo (2 puntos)
            System.out.println("3. AÑADIENDO NUEVO CONSEJO");
            System.out.println("─────────────────────────────────────");
            String nuevoConsejo = "Recuerde pagar su cuenta al día para evitar intereses excesivos y situaciones no deseadas.";
            aniadirConsejo(nuevoConsejo);

            // 4. Reescribir archivo (0.5 puntos)
            System.out.println("4. GUARDANDO ARCHIVO MODIFICADO");
            System.out.println("─────────────────────────────────────");
            reescribirArchivo();

            // Mostrar resumen final
            System.out.println("╔═══════════════════════════════════════════════╗");
            System.out.println("║              RESUMEN DE RESULTADOS            ║");
            System.out.println("╠═══════════════════════════════════════════════╣");
            System.out.println("║ 1. Categorías encontradas: " + categorias.size() + "                  ║");
            System.out.println("║    [historia, estadisticas, consejos]        ║");
            System.out.println("║                                               ║");
            System.out.println("║ 2. Año con mayor uso (> 2020): " + anio + "           ║");
            System.out.println("║                                               ║");
            System.out.println("║ 3. Consejo añadido correctamente             ║");
            System.out.println("║                                               ║");
            System.out.println("║ 4. Archivo guardado: articulo_mod.xml        ║");
            System.out.println("╚═══════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("❌ ERROR GENERAL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicializa el documento XML
     */
    private static void inicializarDocumento() throws Exception {
        File archivo = new File("articulo.xml");

        if (!archivo.exists()) {
            throw new Exception("El archivo 'articulo.xml' no existe");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(archivo);
        doc.getDocumentElement().normalize();

        // Inicializar XPath
        XPathFactory xPathFactory = XPathFactory.newInstance();
        xpath = xPathFactory.newXPath();

        System.out.println("✓ Documento XML cargado correctamente\n");
    }

    /**
     * Crea un archivo XML de ejemplo
     */
    private static void crearArchivoXMLEjemplo() {
        File archivo = new File("articulo.xml");

        if (archivo.exists()) {
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Elemento raíz
            Element articulo = doc.createElement("articulo");
            doc.appendChild(articulo);

            // Sección: Historia
            Element historia = doc.createElement("seccion");
            Element categoriaHistoria = doc.createElement("categoria");
            categoriaHistoria.setTextContent("historia");
            historia.appendChild(categoriaHistoria);

            Element contenidoHistoria = doc.createElement("contenido");
            contenidoHistoria.setTextContent("Las tarjetas de crédito fueron introducidas en la década de 1950...");
            historia.appendChild(contenidoHistoria);

            articulo.appendChild(historia);

            // Sección: Estadísticas
            Element seccionEstadisticas = doc.createElement("seccion");
            Element categoriaEstadisticas = doc.createElement("categoria");
            categoriaEstadisticas.setTextContent("estadisticas");
            seccionEstadisticas.appendChild(categoriaEstadisticas);

            // Estadística 2019
            Element estadistica1 = doc.createElement("estadistica");
            Element anio1 = doc.createElement("anio");
            anio1.setTextContent("2019");
            Element porcentaje1 = doc.createElement("porcentaje");
            porcentaje1.setTextContent("65.5");
            estadistica1.appendChild(anio1);
            estadistica1.appendChild(porcentaje1);
            seccionEstadisticas.appendChild(estadistica1);

            // Estadística 2020
            Element estadistica2 = doc.createElement("estadistica");
            Element anio2 = doc.createElement("anio");
            anio2.setTextContent("2020");
            Element porcentaje2 = doc.createElement("porcentaje");
            porcentaje2.setTextContent("68.2");
            estadistica2.appendChild(anio2);
            estadistica2.appendChild(porcentaje2);
            seccionEstadisticas.appendChild(estadistica2);

            // Estadística 2021
            Element estadistica3 = doc.createElement("estadistica");
            Element anio3 = doc.createElement("anio");
            anio3.setTextContent("2021");
            Element porcentaje3 = doc.createElement("porcentaje");
            porcentaje3.setTextContent("72.8");
            estadistica3.appendChild(anio3);
            estadistica3.appendChild(porcentaje3);
            seccionEstadisticas.appendChild(estadistica3);

            // Estadística 2022
            Element estadistica4 = doc.createElement("estadistica");
            Element anio4 = doc.createElement("anio");
            anio4.setTextContent("2022");
            Element porcentaje4 = doc.createElement("porcentaje");
            porcentaje4.setTextContent("75.3");
            estadistica4.appendChild(anio4);
            estadistica4.appendChild(porcentaje4);
            seccionEstadisticas.appendChild(estadistica4);

            // Estadística 2023
            Element estadistica5 = doc.createElement("estadistica");
            Element anio5 = doc.createElement("anio");
            anio5.setTextContent("2023");
            Element porcentaje5 = doc.createElement("porcentaje");
            porcentaje5.setTextContent("78.9");
            estadistica5.appendChild(anio5);
            estadistica5.appendChild(porcentaje5);
            seccionEstadisticas.appendChild(estadistica5);

            articulo.appendChild(seccionEstadisticas);

            // Sección: Consejos
            Element seccionConsejos = doc.createElement("seccion");
            Element categoriaConsejos = doc.createElement("categoria");
            categoriaConsejos.setTextContent("consejos");
            seccionConsejos.appendChild(categoriaConsejos);

            Element consejos = doc.createElement("consejos");

            Element consejo1 = doc.createElement("consejo");
            consejo1.setAttribute("category", "general");
            consejo1.setTextContent("Utilice su tarjeta de crédito de forma responsable.");
            consejos.appendChild(consejo1);

            Element consejo2 = doc.createElement("consejo");
            consejo2.setAttribute("category", "general");
            consejo2.setTextContent("Revise sus estados de cuenta mensualmente.");
            consejos.appendChild(consejo2);

            seccionConsejos.appendChild(consejos);
            articulo.appendChild(seccionConsejos);

            // Guardar el documento
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);
            transformer.transform(source, result);

            System.out.println("✓ Archivo de ejemplo 'articulo.xml' creado\n");

        } catch (Exception e) {
            System.err.println("Error al crear archivo XML de ejemplo: " + e.getMessage());
        }
    }
}
