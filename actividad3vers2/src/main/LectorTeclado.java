package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;


/*Clase que nos va a permitir
* leer desde el teclado y saca los distintos valores que proporciona el usuario,
* que vamos a usar en las distintas opciones del menú.
* Usa BufferedReader y StringTokenizer, siendo más rápida que la clase Scanner.
*/

public class LectorTeclado {
				
	//Para leer desde un flujo de entrada (en particular, desde el teclado)
	BufferedReader br;
	
	//Para trocear una cadena de caracteres (dividir en tokens)
	//y así poder procesar cada trozo
	StringTokenizer st;
	
	
	//El constructor instancia un BufferedReader que lee desde el teclado
	public LectorTeclado() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	
	//Método que permite obtener lo siguiente que se haya escrito por teclado
	//y lo devuelve como cadena
	//Este método va a ser usado dentro de otros métodos que se desarrollan más adelante
	//para devolver los valores Integer, Double, LocalDate, String, ... 
	private String next() {
		
		//Si el StringTokenizer es nulo o no hay ya más elementos dentro de él
		while (st == null || !st.hasMoreElements()) {
			try {
				//Se instancia el StringTokenizer a partir de la siguiente línea
				//que se haya leído del teclado
				st = new StringTokenizer(br.readLine());
			} catch (IOException ex) { //excepción que puede lanzar readline
				System.err.println("Error de lectura de teclado");
				ex.printStackTrace();
			}
		}
		//En cualquier otro caso devolvemos el siguiente token disponible
		return st.nextToken();
	}
	
	
	//Método para obtener un Integer
	public int nextInt() {
		//Devuelve el parseo a entero de lo que hayamos leído por teclado
		return Integer.parseInt(next());
	}
	
	//Método para obtener un Double
	public double nextDouble() {
		//Devuelve el parseo a double de lo que hayamos leído por teclado
		return Double.parseDouble(next());
	}
	
	//Método para obtener un LocalDate
	public LocalDate nextLocalDate() {
		//Sólo devolverá un LocalDate si lo que obtenemos desde teclado cumple con el patrón dd/MM/yyyy
		return LocalDate.parse(next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	//Método para devolver un String
	public String nextLine() {
		String cadena = "";
		try {
			//Si el StringTokenizer tiene más elementos la cadena será el
			//siguiente token has el salto de línea
			if(st.hasMoreElements()) cadena = st.nextToken("\n");
			//en otro caso, si o hay más elementos,
			// leer directamente la línea desde el BufferedReader
			else cadena = br.readLine();	
		} catch (IOException ex) { //excepción que puede lanzar readline
			System.err.println("Error de lectura de teclado");
			ex.printStackTrace();
		}
		
		return cadena;
	}
}

