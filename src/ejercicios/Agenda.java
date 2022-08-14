package ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Agenda {
	
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 125;
	private static final int NAM_POS = 0;
	private static final int SUR_POS = 1;
	private static final int AGE_POS = 2;
	private static final int TEL_POS = 3;
	private static final int NUM_OF_FIELDS = 4;

	public static void addContact(String name, String surname, int age, String telephone) throws InvalidValueException, IOException {	
		
		// Declaramos en qué directorio se almacenará el fichero.
		File directory = new File(".\\src\\archivos");
		
		// Declaramos en qué fichero se guardan los datos.
		File file = new File(".\\src\\archivos\\agenda.txt");
		
		// Comprobación de la entrada de datos.
		Pattern namePat = Pattern.compile("(?:\\p{L}\\s*)+");
		Matcher m;
		
		// Nombre.
		m = namePat.matcher(name);
		if (!m.matches()) {
			throw new InvalidValueException("El nombre no está formateado correctamente.");
		}
		
		// Apellidos.
		m = namePat.matcher(surname);
		if (!m.matches()) {
			throw new InvalidValueException("Los apellidos no están formateados correctamente.");
		}
		
		// Edad.
		if (age < MIN_AGE || age > MAX_AGE) {
			throw new InvalidValueException("La edad no es válida.");
		}
		
		// Teléfono.
		Pattern telPat = Pattern.compile("^\\d{9}$");
		m = telPat.matcher(telephone);
		if (!m.matches()) {
			throw new InvalidValueException("El teléfono no está formateado correctamente.");
		}
		
		// Si todo está bien formateado, se comprueba si el directorio archivos existe. De no ser así, se creará.
		if (directory.isFile()) {
			throw new IOException("La ruta en la que se debería encontrar el documento está ocupada por un fichero.");
		}
		else if (!directory.isDirectory()) {
			directory.mkdir();
		}
		
		// Se comprueba si existe el fichero.
		if (!file.isDirectory() && !file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new IOException("No se ha podido crear el fichero agenda.txt");
			}
		}
		
		// Ahora que se sabe que el fichero existe, podemos abrir los streams de datos.
		try (BufferedReader input = new BufferedReader(new FileReader(file));
				PrintWriter output = new PrintWriter(new FileWriter(file, true))) {
			
			String l;
			
			// Se comprueba, por cada línea, si el teléfono introducido está repetido.
			while((l = input.readLine()) != null) {
				String[] fields = l.split(":");
				if ((telephone).equals(fields[TEL_POS])) {
					throw new InvalidValueException("El teléfono introducido se encuentra repetido.");
				}
			}
			
			// Una vez comprobado todo, se añaden los datos del contacto.
			output.println(name + ":" + surname + ":" + age + ":" + telephone);
			
		}  catch (IOException e) {
			throw new IOException("Ha ocurrido algún error de I/O: " + e.getMessage());
		}
		
	}
	
	public static void listContacts() throws IOException {
		
		// Declaramos desde qué fichero se leen los datos.
		File file = new File(".\\src\\archivos\\agenda.txt");
		
		// Comprobamos si el fichero existe.
		if (!file.isFile()) {
			throw new IOException("El fichero agenda.txt no existe.");
		}
		
		// Comprobamos si el fichero está vacío.
		if (file.length() == 0) {
			throw new IOException("El fichero agenda.txt está vacío.");
		}
		
		// Si existe, abrimos el stream de datos.
		try (BufferedReader input = new BufferedReader(new FileReader(file))) {
			
			String l;
			int cnt;
			String[] contactInfo = new String[NUM_OF_FIELDS];
			
			// Vamos línea por línea convirtiendo los campos en tokens.
			while ((l = input.readLine()) != null) {
				StringTokenizer fields = new StringTokenizer(l, ":");
				cnt = 0;
				while (fields.hasMoreTokens()) {
					contactInfo[cnt++] = fields.nextToken();
				}
				// Usando el array de Strings que hemos definido, formateamos la información por pantalla.
				String formatString = "********************************************\n"
						+ "CONTACTO\n"
						+ "Nombre: %s\n"
						+ "Apellidos: %s\n"
						+ "Edad: %s\n"
						+ "Teléfono: %s\n"
						+ "********************************************\n";
				System.out.printf(formatString, contactInfo[NAM_POS], contactInfo[SUR_POS], contactInfo[AGE_POS], contactInfo[TEL_POS]);
			}
			
		}
		
	}
	
	public static void XMLExport() throws IOException, XMLException {
		
		//Declaramos desde qué fichero se leerán los datos.
		File file = new File(".\\src\\archivos\\agenda.txt");
		
		// Comprobamos si el fichero existe.
		if (!file.isFile()) {
			throw new IOException("El fichero agenda.txt no existe.");
		}
		
		// Comprobamos si el fichero está vacío.
		if (file.length() == 0) {
			throw new IOException("El fichero agenda.txt está vacío.");
		}
		
		// Declaramos en qué fichero se almacenarán los datos.
		File XMLfile = new File(".\\src\\archivos\\agenda.xml");
		
		try (BufferedReader input = new BufferedReader(new FileReader(file))) {
			
			// Preparamos la construcción del DOM.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			// Preparamos el elemento raíz del XML.
			Element root = doc.createElement("contactos");
			doc.appendChild(root);
			
			String l;
			int cnt;
			String[] contactInfo = new String[NUM_OF_FIELDS];
			
			// Añadimos un elemento por cada línea del documento.
			while ((l = input.readLine()) != null) {
				
				// Separamos los campos del contacto.
				StringTokenizer fields = new StringTokenizer(l, ":");
				cnt = 0;
				while (fields.hasMoreTokens()) {
					contactInfo[cnt++] = fields.nextToken();
				}
				
				// Creamos un elemento contacto.
				Element contact = doc.createElement("contacto");
				
				// Por cada campo, creamos un elemento que almacene su contenido.
				Element name = doc.createElement("nombre");
				name.setTextContent(contactInfo[NAM_POS]);
				
				Element surname = doc.createElement("apellidos");
				surname.setTextContent(contactInfo[SUR_POS]);
				
				Element age = doc.createElement("edad");
				age.setTextContent(contactInfo[AGE_POS]);
				
				Element telephone = doc.createElement("teléfono");
				telephone.setTextContent(contactInfo[TEL_POS]);
				
				// Añadimos cada elemento de los campos al elemento contacto.
				contact.appendChild(name);
				contact.appendChild(surname);
				contact.appendChild(age);
				contact.appendChild(telephone);
				
				// Añadimos el elemento contacto al elemento raíz.
				root.appendChild(contact);
			}
			
			// Normalizamos el documento antes de guardarlo por eficiencia.
			doc.normalize();
			
			// Preparamos la clase transformer que convertirá el DOM a XML.
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			
			// Indicamos la fuente del DOM y dónde se almacenará.
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(XMLfile);
			
			// Realizamos la transformación y el guardado.
			transformer.transform(source, result);
			
		}
		catch (IOException | ParserConfigurationException | TransformerException e) {
			throw new XMLException("Un error ha impedido que se genere el XML.");
		}
		
	}
	
	public static String getMenu() {
		String menu = "[1]Listar Contactos.\n"
				+ "[2]Añadir Contacto.\n"
				+ "[3]Exportar XML.\n"
				+ "[4]Salir.";
		return menu;
	}
	
//	private static int diffDate(LocalDate date1, LocalDate date2) {
//		int diff = date2.getYear() - date1.getYear();
//		if (date1.getMonthValue() > date2.getMonthValue() ||
//				date1.getMonthValue() == date2.getMonthValue() && date1.getDayOfMonth() > date2.getDayOfMonth()) {
//			diff--;
//		}
//		return diff;
//	}
	
}
