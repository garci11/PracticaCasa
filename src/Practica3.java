import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Practica3 {

    /**
     * Lista las diferentes categorías de las secciones
     * Utiliza DOM para parsear el XML (página 38 del PDF)
     */
    public static Set<String> listarCategorias(String archivo) {
        Set<String> categorias = new HashSet<>();
        try {
            // Creación del parser DOM (Document Object Model)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            // Buscamos todas las etiquetas <seccion>
            NodeList secciones = doc.getElementsByTagName("seccion");

            // Recorremos todas las secciones encontradas
            for (int i = 0; i < secciones.getLength(); i++) {
                Element seccion = (Element) secciones.item(i);
                // Obtenemos el atributo "category" de cada sección
                String categoria = seccion.getAttribute("category");
                if (!categoria.isEmpty()) {
                    // HashSet automáticamente evita duplicados
                    categorias.add(categoria);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }

    /**
     * Encuentra el año con mayor porcentaje de uso de tarjeta de crédito
     * Asume una estructura específica en el XML
     */
    public static String encontrarAnioMaximoUsoTarjeta(String archivo) {
        String anioMaximo = "";
        double porcentajeMaximo = -1;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            // Buscar todos los elementos <año>
            NodeList nodos = doc.getElementsByTagName("año");
            for (int i = 0; i < nodos.getLength(); i++) {
                Element añoElement = (Element) nodos.item(i);
                String año = añoElement.getTextContent();

                // Buscar el elemento <porcentaje> asociado a este año
                // Esta implementación asume una estructura específica
                Element siguiente = (Element) añoElement.getNextSibling();
                while (siguiente != null && !siguiente.getNodeName().equals("porcentaje")) {
                    siguiente = (Element) siguiente.getNextSibling();
                }

                if (siguiente != null) {
                    try {
                        double porcentaje = Double.parseDouble(siguiente.getTextContent());
                        if (porcentaje > porcentajeMaximo) {
                            porcentajeMaximo = porcentaje;
                            anioMaximo = año;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar porcentajes no válidos
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar año máximo: " + e.getMessage());
        }
        return anioMaximo;
    }

    /**
     * Añade un nuevo consejo al documento
     * Demuestra modificación de XML con DOM (página 47 del PDF)
     */
    public static void añadirConsejo(String archivo, String nuevoConsejo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            // Buscar la sección de consejos por su atributo category
            NodeList secciones = doc.getElementsByTagName("seccion");
            Element seccionConsejos = null;

            for (int i = 0; i < secciones.getLength(); i++) {
                Element seccion = (Element) secciones.item(i);
                if ("consejos".equals(seccion.getAttribute("category"))) {
                    seccionConsejos = seccion;
                    break;
                }
            }

            if (seccionConsejos != null) {
                // Crear nuevo elemento <consejo>
                Element nuevoConsejoElement = doc.createElement("consejo");
                nuevoConsejoElement.setTextContent(nuevoConsejo);
                nuevoConsejoElement.setAttribute("category", "consejos");

                // Añadir al final de la sección (página 47 del PDF - appendChild)
                seccionConsejos.appendChild(nuevoConsejoElement);

                // Guardar los cambios en el archivo
                guardarDocumento(doc, archivo);
                System.out.println("Consejo añadido correctamente");
            }
        } catch (Exception e) {
            System.err.println("Error al añadir consejo: " + e.getMessage());
        }
    }

    /**
     * Guarda el documento XML modificado
     * Utiliza Transformer para escribir el DOM al archivo (página 40 del PDF)
     */
    private static void guardarDocumento(Document doc, String archivo) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Configurar para que indente el output (más legible)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Convertir el DOM a fuente XML y guardar en archivo
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivo));
            transformer.transform(source, result);
        } catch (Exception e) {
            System.err.println("Error al guardar documento: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String archivoXML = "articulo.xml";

        System.out.println("=== CATEGORÍAS ===");
        Set<String> categorias = listarCategorias(archivoXML);
        System.out.println(categorias);

        System.out.println("\n=== AÑO CON MAYOR USO DE TARJETA ===");
        String añoMaximo = encontrarAnioMaximoUsoTarjeta(archivoXML);
        System.out.println(añoMaximo);

        System.out.println("\n=== AÑADIENDO NUEVO CONSEJO ===");
        String nuevoConsejo = "No dejar al descubierto su cuenta al pagar, para evitar intereses excesivos y situaciones no deseadas";
        añadirConsejo(archivoXML, nuevoConsejo);
    }
}