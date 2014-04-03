package org.init.extractor.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

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
			writer.println(array.toString(2));
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static String readFile (String url)
	{		 
		BufferedReader br = null;
		StringBuffer fileContent = new StringBuffer();
 
		try { 
			String sCurrentLine; 
			br = new BufferedReader(new FileReader(url)); 
			while ((sCurrentLine = br.readLine()) != null) {
				fileContent.append(sCurrentLine);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return fileContent.toString(); 
	}
	
	/**
	 * Convierte un JSON de un solo objeto al formato adecuado de array de objetos
	 * 
	 * @param url la url del JSON source
	 * @param output la url del JSON destino
	 */
	public static void convertJSONToProperFormat(String url, String output)
	{
		String contenido = readFile(url);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(contenido);
		JSONArray result = new JSONArray();
		
        Iterator<?> keys = json.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            JSONObject aux = new JSONObject();
            aux.put("id",key);
            aux.put("nombre",json.get(key));
            result.add(aux);
        }
        crearArchivoJSON(output, result);;
	}
}
