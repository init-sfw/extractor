package org.init.extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Clase que contiene las constantes a utilizar por el extractor
 * 
 * @author fbobbio
 *
 */
public class Constantes {


	/** Constante que mantiene la lista de países y códigos estandarizados que usa Memoria */
	// public static final JSONObject JSON_PAISES = cargarPaises();

	/** Encabezado de url de Wikipedia */
	public static final String ENCABEZADO_URL = "http://es.wikipedia.org/wiki/";
	
	/** Encabezado de url de Wikipedia reducido */
	public static final String ENCABEZADO_CORTO = "http://es.wikipedia.org";
	
	/** Constante de encabezado de la API para búsqueda de una página particular */
	public static final String ENCABEZADO_API_PAGINA = "http://es.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=";
	
	/** Constante de query a la API para traer la lista de páginas de una plantilla particular*/
	public static final String QUERY_API_PLANTILLA = "http://es.wikipedia.org/w/api.php?action=query&prop=revisions&eilimit=500&format=json&list=embeddedin&eititle=Plantilla:";


	/** 
	 * Método que levanta un json de países en formato ISO y lo carga al objeto JSONObject 
	 * para poder utilizarlo en la app y mapear países
	 * 
	 * @return el JSON con todos los países y sus códigos ISO
	 */
	private static JSONObject cargarPaises() {
		//TODO: http://stackoverflow.com/questions/3617160/java-code-to-convert-country-codes-alpha-2-in-to-alpha-3-ind
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("resources/paises.json"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator").toString());
				line = br.readLine();
			}
			String everything = sb.toString();
			return (JSONObject) JSONSerializer.toJSON(everything);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
