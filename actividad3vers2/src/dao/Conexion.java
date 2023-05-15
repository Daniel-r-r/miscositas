package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	/* METODOS PARA CONEXION Y DESCONEXION */
	
	//Crea conexión con BD y la devuelve
    public static Connection conectar(String maquina, String bd, String usuario, String password) {
        Connection con = null;
        try {
            //Carga dinámica del controlador de JDBC para MySQL
        	Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + maquina + "/" + bd, usuario, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la BD");
            e.printStackTrace();
        }
        return con;
    }
    
    
    //Cierra la conexión a la BD
    public static Connection desconectar(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la BD");
            e.printStackTrace();
        }
        return con;
    }
	
    /* FIN DE METODOS PARA CONEXION Y DESCONEXION */
}
