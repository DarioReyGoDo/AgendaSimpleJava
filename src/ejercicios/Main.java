package ejercicios;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	private static final String READ = "1";
	private static final String ADD = "2";
	private static final String XML = "3";
	private static final String EXIT = "4";

	public static void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)) {
			
			boolean exit = false;
			String option;
			String name;
			String surname;
			int age;
			String telephone;
			
			while (!exit) {
				System.out.println(Agenda.getMenu());
				System.out.println("Introduzca la opción que desee ejecutar: ");
				option = sc.nextLine();
				
				switch(option) {
				case READ:
					try {
						Agenda.listContacts();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					break;
				case ADD:
					try {
						// Pedimos los datos.
						System.out.println("Introduzca el nombre: ");
						name = sc.nextLine();
						System.out.println("Introduzca los apellidos: ");
						surname = sc.nextLine();
						System.out.println("Introduzca la edad: ");
						age = Integer.parseInt(sc.nextLine());
						System.out.println("Introduzca el teléfono: ");	
						telephone = sc.nextLine();
						// Intentamos añadir el contacto con los datos introducidos.
						Agenda.addContact(name, surname, age, telephone);
					}
					catch (NumberFormatException e) {
						System.out.println("Debe introducir un número. Cualquier otra cosa no se considerará válida.");
					} catch (InvalidValueException | IOException e) {
						System.out.println(e.getMessage());
					}
					break;
				case XML:
					try {
						Agenda.XMLExport();
						System.out.println("El XML se ha exportado correctamente.");
					} catch (XMLException | IOException e) {
						System.out.println(e.getMessage());
					}
					break;
				case EXIT:
					exit = true;
					break;
				default :
					System.out.println("La opción introducida no es válida.\n");
				}
			}
			
		}

	}

}
