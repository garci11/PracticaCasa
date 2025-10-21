package AccesoDatos.UT1;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Ejercicio8 {

    private static final Path ARCHIVO_XML = Paths.get("alumnos.xml");
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== GESTOR CRUD DE XML (sin try-with-resources) ===\n");

        boolean salir = false;
        while (!salir) {
            System.out.println("1) Crear archivo XML de alumnos");
            System.out.println("2) Leer y mostrar contenido del XML");
            System.out.println("3) Añadir o modificar alumno");
            System.out.println("4) Eliminar alumno por ID");
            System.out.println("5) Salir");
            System.out.print("> ");
            String opcion = sc.nextLine();
            System.out.println();

            switch (opcion) {
                case "1":
                    crearXML();
                    break;
                case "2":
                    leerXML();
                    break;
                case "3":
                    actualizarXML();
                    break;
                case "4":
                    eliminarAlumno();
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

    //  CREAR XML
    private static void crearXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear nodo raíz <alumnos>
            Element root = doc.createElement("alumnos");
            doc.appendChild(root);

            // Crear algunos alumnos iniciales
            Element alumno1 = crearAlumno(doc, "1", "Ana", "8.9");
            Element alumno2 = crearAlumno(doc, "2", "Juan", "7.5");

            root.appendChild(alumno1);
            root.appendChild(alumno2);

            guardarXML(doc);
            System.out.println("Archivo XML creado correctamente: " + ARCHIVO_XML + "\n");

        } catch (Exception e) {
            System.err.println("Error al crear el XML: " + e.getMessage() + "\n");
        }
    }

    // Método auxiliar para crear nodo <alumno>
    private static Element crearAlumno(Document doc, String id, String nombre, String nota) {
        Element alumno = doc.createElement("alumno");
        alumno.setAttribute("id", id);

        Element nom = doc.createElement("nombre");
        nom.setTextContent(nombre);

        Element not = doc.createElement("nota");
        not.setTextContent(nota);

        alumno.appendChild(nom);
        alumno.appendChild(not);

        return alumno;
    }

    //  LEER XML
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

            NodeList lista = doc.getElementsByTagName("alumno");
            System.out.println("Listado de alumnos:");
            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nodo;
                    System.out.println("ID: " + e.getAttribute("id"));
                    System.out.println("Nombre: " + e.getElementsByTagName("nombre").item(0).getTextContent());
                    System.out.println("Nota: " + e.getElementsByTagName("nota").item(0).getTextContent());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el XML: " + e.getMessage() + "\n");
        }
    }

    //  UPDATE XML: añadir o modificar alumno
    private static void actualizarXML() {
        if (!Files.exists(ARCHIVO_XML)) {
            System.out.println("El archivo XML no existe.\n");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ARCHIVO_XML.toFile());
            doc.getDocumentElement().normalize();

            System.out.print("ID del alumno: ");
            String id = sc.nextLine().trim();

            NodeList lista = doc.getElementsByTagName("alumno");
            Element alumnoExistente = null;

            for (int i = 0; i < lista.getLength(); i++) {
                Element e = (Element) lista.item(i);
                if (e.getAttribute("id").equals(id)) {
                    alumnoExistente = e;
                    break;
                }
            }

            if (alumnoExistente != null) {
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = sc.nextLine();
                System.out.print("Nueva nota: ");
                String nuevaNota = sc.nextLine();

                alumnoExistente.getElementsByTagName("nombre").item(0).setTextContent(nuevoNombre);
                alumnoExistente.getElementsByTagName("nota").item(0).setTextContent(nuevaNota);

                System.out.println("Alumno actualizado correctamente.\n");
            } else {
                System.out.println("Alumno no encontrado, se añadirá uno nuevo.\n");
                System.out.print("Nombre: ");
                String nombre = sc.nextLine();
                System.out.print("Nota: ");
                String nota = sc.nextLine();
                Element nuevo = crearAlumno(doc, id, nombre, nota);
                doc.getDocumentElement().appendChild(nuevo);
            }

            guardarXML(doc);
        } catch (Exception e) {
            System.err.println("Error al actualizar el XML: " + e.getMessage() + "\n");
        }
    }

    //  DELETE XML: eliminar alumno por ID
    private static void eliminarAlumno() {
        if (!Files.exists(ARCHIVO_XML)) {
            System.out.println("El archivo XML no existe.\n");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ARCHIVO_XML.toFile());
            doc.getDocumentElement().normalize();

            System.out.print("ID del alumno a eliminar: ");
            String id = sc.nextLine().trim();

            NodeList lista = doc.getElementsByTagName("alumno");
            boolean encontrado = false;

            for (int i = 0; i < lista.getLength(); i++) {
                Element e = (Element) lista.item(i);
                if (e.getAttribute("id").equals(id)) {
                    e.getParentNode().removeChild(e);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarXML(doc);
                System.out.println("Alumno eliminado correctamente.\n");
            } else {
                System.out.println("No se encontró un alumno con ese ID.\n");
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar: " + e.getMessage() + "\n");
        }
    }

    //  Método auxiliar para guardar los cambios en el XML
    private static void guardarXML(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(ARCHIVO_XML.toFile());
            transformer.transform(source, result);
        } catch (Exception e) {
            System.err.println("Error al guardar el XML: " + e.getMessage());
        }
    }
}
