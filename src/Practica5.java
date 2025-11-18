import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class Practica5 {

    /**
     * Lista todas las categorías únicas de productos
     */
    public static Set<String> listarCategoriasProductos(String archivo) {
        Set<String> categorias = new HashSet<>();
        try {
            // Configuración del parser DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            // Buscar todos los elementos producto
            NodeList productos = doc.getElementsByTagName("producto");

            for (int i = 0; i < productos.getLength(); i++) {
                Element producto = (Element) productos.item(i);
                // Obtener categoría del atributo o elemento
                String categoria = producto.getAttribute("categoria");
                if (categoria.isEmpty()) {
                    // Si no tiene atributo, buscar elemento <categoria>
                    NodeList categoriasElem = producto.getElementsByTagName("categoria");
                    if (categoriasElem.getLength() > 0) {
                        categoria = categoriasElem.item(0).getTextContent();
                    }
                }
                if (!categoria.isEmpty()) {
                    categorias.add(categoria);
                }
            }

        } catch (Exception e) {
            System.err.println("Error listando categorías: " + e.getMessage());
        }
        return categorias;
    }

    /**
     * Calcula el valor total del inventario
     */
    public static double calcularValorTotalInventario(String archivo) {
        double valorTotal = 0.0;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            NodeList productos = doc.getElementsByTagName("producto");

            for (int i = 0; i < productos.getLength(); i++) {
                Element producto = (Element) productos.item(i);

                try {
                    // Buscar cantidad y precio
                    int cantidad = Integer.parseInt(
                            producto.getElementsByTagName("cantidad").item(0).getTextContent());
                    double precio = Double.parseDouble(
                            producto.getElementsByTagName("precio").item(0).getTextContent());

                    valorTotal += cantidad * precio;

                } catch (NumberFormatException | NullPointerException e) {
                    System.err.println("Error procesando producto " + (i+1));
                }
            }

        } catch (Exception e) {
            System.err.println("Error calculando inventario: " + e.getMessage());
        }
        return valorTotal;
    }

    /**
     * Añade un nuevo producto al inventario
     */
    public static void añadirProducto(String archivo, String nombre, String categoria,
                                      int cantidad, double precio) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(archivo));

            // Obtener el elemento raíz
            Element raiz = doc.getDocumentElement();

            // Crear nuevo elemento producto
            Element nuevoProducto = doc.createElement("producto");
            nuevoProducto.setAttribute("categoria", categoria);

            // Crear y añadir subelementos
            Element elemNombre = doc.createElement("nombre");
            elemNombre.setTextContent(nombre);
            nuevoProducto.appendChild(elemNombre);

            Element elemCantidad = doc.createElement("cantidad");
            elemCantidad.setTextContent(String.valueOf(cantidad));
            nuevoProducto.appendChild(elemCantidad);

            Element elemPrecio = doc.createElement("precio");
            elemPrecio.setTextContent(String.valueOf(precio));
            nuevoProducto.appendChild(elemPrecio);

            // Añadir el nuevo producto al raíz
            raiz.appendChild(nuevoProducto);

            // Guardar cambios
            guardarDocumentoXML(doc, archivo);
            System.out.println("Producto añadido correctamente");

        } catch (Exception e) {
            System.err.println("Error añadiendo producto: " + e.getMessage());
        }
    }

    /**
     * Guarda el documento XML
     */
    private static void guardarDocumentoXML(Document doc, String archivo) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivo));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.err.println("Error guardando XML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String archivo = "inventario.xml";

        System.out.println("=== CATEGORÍAS DE PRODUCTOS ===");
        Set<String> categorias = listarCategoriasProductos(archivo);
        categorias.forEach(System.out::println);

        System.out.println("\n=== VALOR TOTAL INVENTARIO ===");
        double valorTotal = calcularValorTotalInventario(archivo);
        System.out.printf("Valor total: %.2f€\n", valorTotal);

        System.out.println("\n=== AÑADIENDO NUEVO PRODUCTO ===");
        añadirProducto(archivo, "Tablet Samsung", "electrónica", 15, 299.99);
    }
}

//Listar categorías de productos
//
//Calcular valor total del inventario
//
//Añadir nuevo producto