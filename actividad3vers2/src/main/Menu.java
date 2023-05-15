package main;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import dao.AlumnoDAO;
import pojo.Alumno;

public class Menu {
	
	private LectorTeclado lector;
	private AlumnoDAO alumnoDAO;
	
	
	public Menu() {
		lector = new LectorTeclado();
		alumnoDAO = AlumnoDAO.getInstance();
	}
	
	
	
	public void init() {
		
		int opcion;
		
		do {
			
			menu();
			opcion = lector.nextInt();
			
			switch (opcion) {
			
			case 1:
				insertar();
				break;
			case 2: 
				buscarAlumno();
				break;
			case 3: 
				actualizar();
				break;
			case 4: 
				eliminar();
				break;
			case 5: 
				mostrarTodosAlumnos();
				break;
			case 6: 
				mostrarTodosAlumnosCurso();
				break;
			case 7:	
				System.out.println("\nSaliendo de la aplicación ...\n");
				finalizar();
				System.out.println(" ... ¡¡Listo!! Hasta pronto ;-)\n");
				break;
			default:
				System.err.println("\nOpción no correcta\n\n");
			}
			
		} while(opcion != 7);
	}
	
	
	
	
	public void menu() {
		
		System.out.println("SISTEMA DE GESTIÓN DE ALUMNOS");
		System.out.println("=============================");
		System.out.println("Introduzca una opción de las siguientes: \n");
		System.out.println("1. Insertar un alumno");
        System.out.println("2. Buscar un alumno");
        System.out.println("3. Actualizar los datos de un alumno");
        System.out.println("4. Eliminar un alumno");
        System.out.println("5. Listar todos los alumnos");
        System.out.println("6. Listar alumnos de un curso");
        System.out.println("7. Salir");
        System.out.println("\nOpción: ");
		
	}
	
	
	
	/* 
	 * MÉTODO PARA IMPRIMIR LA CABECERA DEL LISTADO DE ALUMNOS
	 */
	private void printCabeceraListadoAlumnos() {
		System.out.printf("%8s %35s %15s %15s %10s","N_ALUMNO","NOMBRE","FECHA_NAC.","NOTA_MEDIA","CURSO");
		System.out.println("");
		IntStream.range(1, 100).forEach(x -> System.out.print("-")); //Imprime 100 guiones
		System.out.println("\n");
	}
	
	
	/* 
	 * MÉTODO PARA IMPRIMIR UN ALUMNO
	 */
	private void printAlumno(Alumno alumno) {
		System.out.printf("%8s %35s %15s %15s %10s\n",
							alumno.getId(),
							alumno.getNombre(),
							alumno.getfNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
							alumno.getNotaMedia(),
							alumno.getCurso());
	}
	
	
	
	/* 
	 * MÉTODO QUE CIERRA SESIÓN LLAMADO POR LA OPCION DE MENU AL SALIR
	 */
	private void finalizar() {
		alumnoDAO.fin();
	}
	
	
	
	/* 
	 * MÉTODO DE INSERCIÓN PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void insertar() {
		System.out.println("\nINSERCIÓN DE UN NUEVO ALUMNO");
		System.out.println("------------------------------\n");
		
		System.out.print("Nombre y apellidos: ");
		String nombre = lector.nextLine();
		
		//NO estamos asegurando que el usuario introduzca la fecha con formato dd/mm/aaaa
		System.out.print("Fecha de nacimiento (formato: dd/mm/aaaa): ");
        LocalDate fechaNac = lector.nextLocalDate();
        
        System.out.print("Nota media: ");
        double media = lector.nextDouble();
        
        System.out.print("Curso: ");
        String curso = lector.nextLine();
        
        try {
        	alumnoDAO.create(new Alumno(nombre, fechaNac, media, curso));
        	System.out.println("Nuevo alumno insertado");
        } catch (SQLException e) {
			System.out.println("Error insertando registro en la base de datos");
        	e.printStackTrace();
        }
        
        System.out.println("");	
	}
	
	
	
	/* 
	 * MÉTODO BUSCAR ALUMNO PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void buscarAlumno() {
		System.out.println("\nBUSCAR ALUMNO");
		System.out.println("------------------------------\n");
		
		try {
			
			System.out.print("\nIntroduzca el número del alumno que desea buscar: ");
            int numBuscado = lector.nextInt();
            
            Alumno alumno = alumnoDAO.read(numBuscado);
            
            if (alumno == null) {
                System.out.println("No se ha encontrado ningún alumno con el número " + numBuscado);
            } else {
            	printCabeceraListadoAlumnos();
            	printAlumno(alumno);
            }
            System.out.println("\n");
			
		} catch (SQLException e) {
			System.out.println("Error consultando la base de datos");
			e.printStackTrace();
		}
	}
	
	
	
	/* 
	 * MÉTODO DE ACTUALIZACIÓN PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void actualizar() {
		System.out.println("\nACTUALIZACION DATOS DE ALUMNO");
		System.out.println("-------------------------------\n");
		
		try {
			
			System.out.print("\nIntroduzca el número del alumno que desea actualizar: ");
            int numBuscado = lector.nextInt();
            
            Alumno alumno = alumnoDAO.read(numBuscado);
            
            if (alumno == null) {
                System.out.println("No se ha encontrado ningún alumno con el número " + numBuscado);
            } else {
            	printCabeceraListadoAlumnos();
            	printAlumno(alumno);
            	
            	System.out.println("\n");
            	System.out.println("A continuación indicar las modificaciones en cada campo\n"
            			+ "o pulsar Intro si el campo no se modifica");
            	System.out.println("\n");
            	
            	System.out.printf("Nombre del alumno (%s): ", alumno.getNombre());
            	String nombre = lector.nextLine();
            	nombre = (nombre.isBlank()) ? alumno.getNombre() : nombre;
            	
            	System.out.printf("Fecha nacimiento (formato dd/MM/aaaa) (%s): ",
            			alumno.getfNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            	String strfechaNacimiento = lector.nextLine();
            	LocalDate fechaNacimiento = (strfechaNacimiento.isBlank()) ? alumno.getfNacimiento() :
            			LocalDate.parse(strfechaNacimiento,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            	
            	DecimalFormatSymbols separadorDecimal = new DecimalFormatSymbols();
            	separadorDecimal.setDecimalSeparator('.');
            	DecimalFormat formatoNumero = new DecimalFormat("##.#",separadorDecimal);
            	System.out.println("Nota media ("+
            						formatoNumero.format(alumno.getNotaMedia())+"): ");
            	String strNotaMedia = lector.nextLine();
            	Double notaMedia = (strNotaMedia.isBlank()) ? alumno.getNotaMedia() : Double.parseDouble(strNotaMedia);
            	
            	System.out.printf("Curso (%s): ", alumno.getCurso());
            	String curso = lector.nextLine();
            	curso = (curso.isBlank()) ? alumno.getCurso() : curso;
            	
            	alumno.setNombre(nombre);
            	alumno.setfNacimiento(fechaNacimiento);
            	alumno.setNotaMedia(notaMedia);
            	alumno.setCurso(curso);
            	
            	alumnoDAO.update(alumno);
            	
            	System.out.println("");
            	System.out.printf("El alumno con número %s ha sido actualizado", numBuscado);
            	
            }
            System.out.println("\n");
            
			
		} catch (SQLException e) {
			System.out.println("Error consultando la base de datos");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/* 
	 * MÉTODO DE ELIMINACION PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void eliminar() {
		System.out.println("\nELIMINAR ALUMNO");
		System.out.println("-------------------------------\n");
		
		try {
			
			System.out.print("\nIntroduzca el número del alumno que desea eliminar: ");
            int numBuscado = lector.nextInt();
            
            Alumno alumno = alumnoDAO.read(numBuscado);
            
            if (alumno == null) {
                System.out.println("No se ha encontrado ningún alumno con el número " + numBuscado);
            } else {
            	
            	System.out.println("¿Seguro de que se quiere eliminar al alumno con número "+ numBuscado+"? (s/n)");
            	String eliminar = lector.nextLine();
            	
            	if (eliminar.equalsIgnoreCase("s")) {
            		alumnoDAO.delete(numBuscado);
            		System.out.println("Se ha eliminado el alumno con el número " + numBuscado);
            	}
            	else System.out.println("El alumno con el número " + numBuscado+" no ha sido eliminado");
            }
            System.out.println("\n");
			
		} catch (SQLException e) {
			System.out.println("Error consultando la base de datos");
			e.printStackTrace();
		}		
		
	}
	
	
	
	/* 
	 * MÉTODO QUE IMPRIME EL LISTADO DE TODOS LOS ALUMNOS. PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void mostrarTodosAlumnos() {
		System.out.println("\nLISTADO DE TODOS LOS ALUMNOS");
		System.out.println("------------------------------\n");
		
		try {
			List<Alumno> listadoAlumnos = alumnoDAO.todosAlumnos();
			if (listadoAlumnos.isEmpty()) System.out.println("No hay alumnos registrados");
			else {
				printCabeceraListadoAlumnos();
				listadoAlumnos.forEach(this::printAlumno); //Imprimo todos los alumnos de la lista
			}
		} catch (SQLException e) {
			System.out.println("Error consultando la base de datos");
			e.printStackTrace();
		}
		System.out.println("\n");
	}
	
	
	
	/* 
	 * MÉTODO QUE IMPRIME EL LISTADO DE TODOS LOS ALUMNOS DE UN CURSO. PARA SER LLAMADO POR LA OPCIÓN DE MENÚ
	 */
	public void mostrarTodosAlumnosCurso() {
		System.out.println("\nLISTADO DE TODOS LOS ALUMNOS DE UN CURSO");
		System.out.println("------------------------------------------\n");
		
		try {
			
			System.out.print("\nIndique el curso cuyos alumnos desea listar: ");
            String cursoBuscado = lector.nextLine();
            
            List<Alumno> listadoAlumnos = alumnoDAO.alumnosXCurso(cursoBuscado);
			
			if(listadoAlumnos.isEmpty())
				System.out.println("No hay alumnos registrados en el curso "+cursoBuscado);
			else {
				printCabeceraListadoAlumnos();
				listadoAlumnos.forEach(this::printAlumno); //Imprimo todos los alumnos de la lista
			}
		} catch (SQLException e) {
			System.out.println("Error consultando la base de datos");
			e.printStackTrace();
		}
		System.out.println("\n");
	}
	
	
	
	
}
