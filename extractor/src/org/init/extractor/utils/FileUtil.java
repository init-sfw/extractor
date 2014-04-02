package org.init.extractor.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONArray;

/**
 * Clase que posee utilidades para el manejo de archivos
 * 
 * @author fbobbio
 *
 */
public class FileUtil {

	/**
	 * Método que escribe a disco 
	 * 
	 * @param array
	 */
	public static void crearArchivoJSON(String nombreArchivo, JSONArray array) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(nombreArchivo, "UTF-8");
			writer.println(array.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que convierte el valor de país de wikipedia al código definido por la ISO
	 * 
	 * @param string
	 * @return
	 */
	public static Object convertirPaisAISO(String string) {
		return null;
	}
	
	/**
	 * Convierte un JSON de un solo objeto al formato adecuado de array de objetos
	 * 
	 * @param url la url del JSON source
	 * @param output la url del JSON destino
	 */
	public static void convertJSONISOToProperFormat(String url, String output)
	{
		 //f = leerJSON(url);
	}

}
