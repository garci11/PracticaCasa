package AccesoDatos.UT1;

//Crea un programa que genere un XML llamado alumnos.xml con esta estructura:
//<alumnos>
//  <alumno id="1">
//    <nombre>Ana</nombre>
//    <nota>9.2</nota>
//  </alumno>
//  <alumno id="2">
//    <nombre>Juan</nombre>
//    <nota>8.5</nota>
//  </alumno>
//</alumnos>
//Modifica el XML anterior para:
//Añadir un nuevo alumno.
//Cambiar la nota de uno existente.
//Borrar uno de los nodos <alumno>.

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;

public class Ejercicio4 {
    private static final Path ARCHIVO_XML = Paths.get("alumnos.xml");
    public static void main(String[] args) {
        crearXML();
        leerXML();
        modificarXML();
        eliminarNodo();
        leerXML(); // mostrar resultado final
        System.out.println("\nPrograma finalizado.");
    }

    // Crear un archivo XML desde cero
    private static void crearXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Crear documento vacío
            Document doc = builder.newDocument();

            // Nodo raíz <alumnos>
            Element root = doc.createElement("alumnos");
            doc.appendChild(root);

            // Primer alumno
            Element alumno1 = doc.createElement("alumno");
            alumno1.setAttribute("id", "1");

            Element nombre1 = doc.createElement("nombre");
            nombre1.setTextContent("Ana");
            alumno1.appendChild(nombre1);

            Element nota1 = doc.createElement("nota");
            nota1.setTextContent("9.2");
            alumno1.appendChild(nota1);

            root.appendChild(alumno1);

            // Segundo alumno
            Element alumno2 = doc.createElement("alumno");
            alumno2.setAttribute("id", "2");

            Element nombre2 = doc.createElement("nombre");
            nombre2.setTextContent("Juan");
            alumno2.appendChild(nombre2);

            Element nota2 = doc.createElement("nota");
            nota2.setTextContent("8.5");
            alumno2.appendChild(nota2);

            root.appendChild(alumno2);

            guardarDocumento(doc);
            System.out.println("Archivo XML creado correctamente: " + ARCHIVO_XML.getFileName() + "\n");

        } catch (Exception e) {
            System.err.println("Error al crear XML: " + e.getMessage());
        }
    }

    // Leer el contenido del archivo XML
    private static void leerXML() {
        if (!Files.exists(ARCHIVO_XML)) {
            System.out.println("El archivo XML no existe.\n");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ARCHIVO_XML.toFile());
            doc.getDocumentElement().normalize();

            System.out.println("Contenido del XML:");
            NodeList lista = doc.getElementsByTagName("alumno");

            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element alumno = (Element) nodo;
                    String id = alumno.getAttribute("id");
                    String nombre = alumno.getElementsByTagName("nombre").item(0).getTextContent();
                    String nota = alumno.getElementsByTagName("nota").item(0).getTextContent();

                    System.out.println("Alumno ID: " + id);
                    System.out.println("  Nombre: " + nombre);
                    System.out.println("  Nota: " + nota);
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error al leer XML: " + e.getMessage());
        }
    }

    //  Modificar un nodo existente (cambiar nota o añadir nuevo alumno)
    private static void modificarXML() {
        if (!Files.exists(ARCHIVO_XML)) {
            System.out.println("El archivo XML no existe.\n");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ARCHIVO_XML.toFile());
            doc.getDocumentElement().normalize();

            // Cambiar nota del alumno con id="2"
            NodeList lista = doc.getElementsByTagName("alumno");
            for (int i = 0; i < lista.getLength(); i++) {
                Element alumno = (Element) lista.item(i);
                if (alumno.getAttribute("id").equals("2")) {
                    alumno.getElementsByTagName("nota").item(0).setTextContent("9.0");
                    System.out.println("Nota del alumno 2 actualizada a 9.0");
                }
            }

            // Añadir un nuevo alumno (id=3)
            Element nuevo = doc.createElement("alumno");
            nuevo.setAttribute("id", "3");

            Element nombre3 = doc.createElement("nombre");
            nombre3.setTextContent("Lucía");
            nuevo.appendChild(nombre3);

            Element nota3 = doc.createElement("nota");
            nota3.setTextContent("7.8");
            nuevo.appendChild(nota3);

            doc.getDocumentElement().appendChild(nuevo);

            guardarDocumento(doc);
            System.out.println("Nuevo alumno añadido (id=3).\n");

        } catch (Exception e) {
            System.err.println("Error al modificar XML: " + e.getMessage());
        }
    }

    //  Eliminar un nodo (por ejemplo, alumno con id="1")
    private static void eliminarNodo() {
        if (!Files.exists(ARCHIVO_XML)) {
            System.out.println("El archivo XML no existe.\n");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ARCHIVO_XML.toFile());
            doc.getDocumentElement().normalize();

            NodeList lista = doc.getElementsByTagName("alumno");
            for (int i = 0; i < lista.getLength(); i++) {
                Element alumno = (Element) lista.item(i);
                if (alumno.getAttribute("id").equals("1")) {
                    alumno.getParentNode().removeChild(alumno);
                    System.out.println("Alumno con id=1 eliminado.");
                    break;
                }
            }

            guardarDocumento(doc);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Error al eliminar nodo XML: " + e.getMessage());
        }
    }

    // Método auxiliar: guarda el documento XML en disco
    private static void guardarDocumento(Document doc) {
        TransformerFactory tf = null;
        Transformer transformer = null;
        try {
            tf = TransformerFactory.newInstance();
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(ARCHIVO_XML.toFile());
            transformer.transform(source, result);
        } catch (TransformerException e) {
            System.err.println("Error al guardar el XML: " + e.getMessage());
        }
    }
}
