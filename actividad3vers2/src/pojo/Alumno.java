package pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Alumno {
	
	private final int TAM_NOMBRE = 30; //tamaño máximo del nombre, que en la BD se ha definido como un VARCHAR (30)
	private int id; //atributo identificador
	private String nombre;
	private LocalDate fNacimiento;
	private double notaMedia;
	private String curso; //La cadena curso debe tener una longitud máx. de 2
	
	
	//Constructor vacío
	public Alumno () { }
	
	/* Constructor que crea un objeto Alumno únicamente con su identificador.
	 * Se utiliza para leer los datos desde la BD a partir de la clave. */ 
	public Alumno (int id) {
		this.id = id;
	}
	
	public Alumno (String nombre, LocalDate fNacimiento, double notaMedia, String curso) {
		this.setNombre (nombre);
		this.fNacimiento = fNacimiento;
		this.notaMedia = notaMedia;
		this.setCurso (curso);

	}
	
	public Alumno (int id, String nombre, LocalDate fNacimiento, double notaMedia, String curso) {
		this(nombre, fNacimiento, notaMedia, curso);
		this.id = id;
	}
	
	
	public int getId() { return id;	}
	public void setId(int id) {	this.id = id;}


	public String getNombre() { return nombre; }
	//Limita la longitud del nombre al número de caracteres indicado por la constante TAM_NOMBRE
	public void setNombre(String nombre) {
		this.nombre = nombre.substring(0, Math.min(TAM_NOMBRE, nombre.length()));
	}


	public LocalDate getfNacimiento() { return fNacimiento; }
	public void setfNacimiento(LocalDate fNacimiento) {	this.fNacimiento = fNacimiento;	}


	public double getNotaMedia() { return notaMedia; }
	public void setNotaMedia(double notaMedia) { this.notaMedia = notaMedia; }


	public String getCurso() { return curso; }
	//En la BD es un varchar(2). Tenemos que limitar el String a un máximo de 2 caracteres
	public void setCurso(String curso) {
		this.curso = curso.substring(0, Math.min(2, nombre.length()));
	}
	
	


	@Override
	public int hashCode() {
		return Objects.hash(TAM_NOMBRE, curso, fNacimiento, id, nombre, notaMedia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alumno other = (Alumno) obj;
		return TAM_NOMBRE == other.TAM_NOMBRE && Objects.equals(curso, other.curso)
				&& Objects.equals(fNacimiento, other.fNacimiento) && id == other.id
				&& Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(notaMedia) == Double.doubleToLongBits(other.notaMedia);
	}

	@Override
	public String toString() {
		DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return "Alumno{id = "+id+", nombre = "+nombre+", fNacimiento = "+fNacimiento.format(formatoFecha)+
				", notaMedia = "+notaMedia+", curso = "+curso+"}";
	}
}
