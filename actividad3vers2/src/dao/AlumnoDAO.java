package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pojo.Alumno;
import dao.Conexion;

public class AlumnoDAO{
	
	private Connection conexion;
	private final String USUARIO="root";
	private final String PASSWORD = "";
    private final String MAQUINA = "localhost";
    private final String BD = "instituto";
    
    public Connection getConnection() {
    	return this.conexion;
    }
          
	
	/*PATRON SINGLETON
	 * Lo necesitamos para tener una única instancia de AlumnoDAO
	 * para toda la aplicación, y así no tener que ir instanciándola
	 * tantas veces como vayamos necesitándola.
	 * Declaramos, por tanto, "que el objeto va a ser singleton".
	 * Para ello:
	 * 1. Declaramos una referencia privada estática de la propia clase
	 * 2. Declaramos un bloque estático donde se instancie este objeto
	 * 3. El constructor lo declaramos privado y sin argumentos
	 * 4. Declaramos un método público estático que nos devolverá una instancia
	 */
	
	//1. Declaramos una referencia privada estática de la propia clase
	//   (le llamamos "instance")
	private static AlumnoDAO instance; 
	
	//2. Declaramos un bloque estático donde se instancie este objeto
	static {
		instance = new AlumnoDAO();
	}
	
	//3. El constructor lo declaramos privado y sin argumentos, de manera que
	//   nadie lo pueda invocar desde fuera
	private AlumnoDAO() {
		//En el constructor creamos la conexión,
		//que se mantendrá abierta todo el tiempo.
		this.conexion = Conexion.conectar(MAQUINA,BD,USUARIO,PASSWORD);
	}
	
	//4. Declaramos un método público estático que nos devolverá una instancia
	//   de este objeto
	public static AlumnoDAO getInstance() {
		return instance;
	}
	
	/*FIN DE IMPLEMENTACIÓN DEL PATRON SINGLETON*/
	
	
	
	/* MÉTODO QUE PIDE EL CIERRE DE LA CONEXIÓN A LA BD  ***/
	public void fin() {
		Conexion.desconectar(this.conexion);
	}
	
	
    
    
	
	/* MÉTODO create --> INSERT  ***/
	public int create(Alumno alumno) throws SQLException { //El método que invoque a add
								  //será el responsable de tratar las excepciones
		
		//El num del alumno no se indica porque al ser autogenerado
		//ya se ocupa MySQL de crearlo
		String sql = """
					INSERT INTO alumnos (nombre, fechanac, media, curso)
					VALUES (?, ?, ?, ? );
				""" ;
		
		int result;
		
		try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
			sentencia.setString (1, alumno.getNombre());
			//Transformamos un java.time.LocalDate a un java.sql.Date
			sentencia.setDate (2, Date.valueOf(alumno.getfNacimiento()));
			sentencia.setDouble (3, alumno.getNotaMedia());
			sentencia.setString (4, alumno.getCurso());
			
			result = sentencia.executeUpdate();
        } 
		return result;
	}
	/* FIN MÉTODO create --> INSERT  ***/
	
	
	
	/* MÉTODO read --> SELECT * WHERE num = ***/
	public Alumno read(int id) throws SQLException {
		
		Alumno alumno = null; //Si no encontramos al alumno con el id especificado, devuelve null
        
		String sql = "SELECT * FROM alumnos WHERE num = ?";
        
		try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            
        	sentencia.setInt(1, id); //Dentro del primer try no podemos setear el parámetro,
            						 //por lo que necesitamos un segundo try anidado para poder seguir
            						 //usando try-with-resources
        	
            try (ResultSet rs = sentencia.executeQuery()) {
                
            	if (rs.next()) { //si hay un registro. No uso while porque se supone que devuelve uno o ninguno
                	
            		String nombre = rs.getString("nombre");
            		//Transformamos el java.sql.Date en un java.time.LocalDate
					LocalDate fNacimiento = rs.getDate("fechanac").toLocalDate();
					Double notaMedia = rs.getDouble ("media");
					String curso = rs.getString("curso");
					
					//creamos un objeto con los datos obtenidos
					alumno = new Alumno (id, nombre, fNacimiento, notaMedia, curso);
                }
            }
        }
        return alumno;
	}
	/* FIN MÉTODO read --> SELECT * WHERE num = ***/
	
	
	/* MÉTODO update --> UPDATE ***/
	public int update(Alumno alumno) throws SQLException {
		
		String sql = """
					UPDATE alumnos SET
					nombre=?, fechanac=?, media=?, curso=?
					WHERE num=?
				""";
		int result;
		
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
        	
        	sentencia.setString (1, alumno.getNombre());
        	//Transformamos un java.time.LocalDate a un java.sql.Date
			sentencia.setDate (2, Date.valueOf(alumno.getfNacimiento()));
			sentencia.setDouble (3, alumno.getNotaMedia());
			sentencia.setString (4, alumno.getCurso());
			sentencia.setInt(5, alumno.getId());
			
            result = sentencia.executeUpdate();
        }
		return result;
	}
	/* FIN MÉTODO update --> UPDATE ***/
	
	
	
	/* MÉTODO delete --> DELETE ***/
	/* Si necesitasemos una garantía del borrado, podemos devolver un entero
	 * pero habitualmente los métodos de borrado no devuelven nada
	 */
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM alumnos WHERE num=?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }	
	}
	/* FIN MÉTODO delete --> DELETE ***/
	
	
	
	/* MÉTODO todosAlumnos --> SELECT *  ***/
	public List<Alumno> todosAlumnos() throws SQLException {

        List<Alumno> alumnos = new ArrayList<>(); //Siempre se va a devolver una lista, aunque puede estar vacía
        String sql = "SELECT * FROM alumnos";
        try (Statement sentencia = conexion.createStatement();
        	 
        	ResultSet rs = sentencia.executeQuery(sql)) {
        	
            while (rs.next()) {
            	int id = rs.getInt("num");
            	String nombre = rs.getString("nombre");
            	//Transformamos el java.sql.Date en un java.time.LocalDate
				LocalDate fNacimiento = rs.getDate("fechanac").toLocalDate();
				Double notaMedia = rs.getDouble ("media");
				String curso = rs.getString("curso");
                
				//creamos un objeto con los datos obtenidos y lo añadimos a la lista
				alumnos.add(new Alumno(id,nombre,fNacimiento,notaMedia,curso));
            }
        }
        return alumnos;
	}
	/* FIN MÉTODO todosAlumnos --> SELECT *  ***/
	
	
	
	/* MÉTODO alumnosXCurso --> SELECT * WHERE curso = ***/
	public List<Alumno> alumnosXCurso(String curso) throws SQLException {
        
		List<Alumno> alumnos = new ArrayList<>(); //Siempre se va a devolver una lista, aunque puede estar vacía
		
        String sql = "SELECT * FROM alumnos WHERE curso = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
        	
        	sentencia.setString(1, curso);//Dentro del primer try no podemos setear el parámetro,
			 							  //por lo que necesitamos un segundo try anidado para poder seguir
			 							  //usando try-with-resources
            
        	try (ResultSet rs = sentencia.executeQuery()) {
               
        		while (rs.next()) {
                	int id = rs.getInt("num");
                	String nombre = rs.getString("nombre");
                	//Transformamos el java.sql.Date en un java.time.LocalDate
                	LocalDate fNacimiento = rs.getDate("fechanac").toLocalDate();
    				Double notaMedia = rs.getDouble ("media");
                    
    				//creamos un objeto con los datos obtenidos y lo añadimos a la lista
    				alumnos.add(new Alumno(id,nombre,fNacimiento,notaMedia,curso));
                }
            }
        }
        return alumnos;
    }
	/* FIN MÉTODO alumnosXCurso --> SELECT * WHERE curso = ***/
	
	
}
