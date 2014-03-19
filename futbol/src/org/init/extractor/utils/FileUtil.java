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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
