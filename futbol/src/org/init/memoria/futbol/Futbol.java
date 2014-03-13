package org.init.memoria.futbol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 
 * Clase que se encarga de extraer datos desde wikipedia sobre torneos de fútbol
 * y generar una salida en formato json que Memoria-web pueda consumir
 * 
 * @author fbobbio
 * 
 */
public class Futbol {

	// public static final JSONObject JSON_PAISES = cargarPaises();

	public static final String ENCABEZADO_API = "http://es.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=";

	public static final String ENCABEZADO_URL = "http://es.wikipedia.org/";

	public static void main(String[] args) {
		init();
	}

	private static JSONObject cargarPaises() {
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

	public static void init() {
		// Ejecuto el request de las páginas a wikipedia a través de su API HTML
		String cadenaPaginas = "http://es.wikipedia.org/w/api.php?action=query&prop=revisions&list=embeddedin&eititle=Plantilla:Ficha_de_torneo_de_f%C3%BAtbol&eilimit=500&format=json";
		String stringListaPaginas = requestGeneral(cadenaPaginas);
		JSONObject listaPaginas = convertStringToJSONObject(stringListaPaginas);

		// System.out.println(listaPaginas.toString());

		// Tomo el array del json prosesado con la lista de las páginas
		JSONArray arrayProcesado = listaPaginas.getJSONArray("array");

		// Array con todos los infoboxes de las páginas que se pedirán
		JSONArray arrayInfoboxes = new JSONArray();

		// Recorro el arrayProcesado, busco cada una de sus páginas y traigo el
		// resultado en json que guardo en arrayInfoboxes
		for (int i = 0; i < 74; i++) { // TODO: arrayProcesado.size()
			JSONObject object = (JSONObject) arrayProcesado.get(i);
			String titulo = object.get("title").toString();
			String pageid = object.get("pageid").toString();
			String url = ENCABEZADO_API + titulo.replace(' ', '_');
			String infobox = requestGeneral(url);
			ArrayList<JSONObject> aux = convertirInfoboxAMemoria(infobox, titulo, pageid);
			arrayInfoboxes.addAll(aux);

			System.out.println(i);
		}
		
		crearArchivoJSON(arrayInfoboxes);
		System.out.println("\n\n\n" + arrayInfoboxes);
	}

	/** 
	 * Método que crea el archivo de json para ser consumido por Memoria
	 * 
	 * @param arrayInfoboxes
	 */
	private static void crearArchivoJSON(JSONArray arrayInfoboxes) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("datos-mundial.json", "UTF-8");
			writer.println(arrayInfoboxes.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Método que realiza el request general a la lista de páginas que nos
	 * interesan
	 * 
	 * @return string intermedio con las páginas particulares y sus direcciones
	 */
	public static String requestGeneral(String cadena) {
		// Defino variables
		URL url = null;
		try {
			url = new URL(cadena);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection connection = null;
		StringBuilder sb = null;
		String line = null;

		// Realizo la conexión y pido el retorno
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			// read the result from the server
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the connection, set all objects to null
			connection.disconnect();
		}

		return sb.toString();
	}

	/**
	 * Método que convierte el string original devuelto por el request a json
	 * 
	 * @param string
	 * @return
	 */
	private static JSONObject convertStringToJSONObject(String originalJson) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(originalJson);
		JSONArray jsonQuery = json.getJSONObject("query").getJSONArray(
				"embeddedin");

		// Creo el jsonArray a devolver luego
		JSONArray array = new JSONArray();

		// Itero el array y creo un nuevo json
		for (int i = 0; i < jsonQuery.size(); i++) {
			JSONObject aux = new JSONObject();
			JSONObject object = jsonQuery.getJSONObject(i);
			String id = object.getString("pageid");
			String title = object.getString("title");
			aux.put("pageid", id);
			aux.put("title", title);
			array.add(aux);
		}

		JSONObject ret = new JSONObject();
		ret.put("array", array);

		return ret;
	}

	/**
	 * Método que convierte el string del infobox de cada página consultada a un
	 * json en formato Memoria que podrá utilizarse directamente en la app.
	 * 
	 * @param infobox
	 *            un string de json en formato wikipedia con un template
	 *            particular de infobox
	 * @param titulo
	 *            el título del evento o la página
	 * @pageid pageid el número de identificación de la página que estamos
	 *         recorriendo
	 * @return el infobox convertido a formato json memoria
	 */
	private static ArrayList<JSONObject> convertirInfoboxAMemoria(String infobox,
			String titulo, String pageid) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(infobox);
		JSONArray jsonQuery = json.getJSONObject("query")
				.getJSONObject("pages").getJSONObject(pageid)
				.getJSONArray("revisions");

		// Creo el jsonArray a devolver luego
		ArrayList<JSONObject> array = new ArrayList<JSONObject>();

		// Itero el array y creo un nuevo json
		for (int i = 0; i < jsonQuery.size(); i++) {
			JSONObject aux = new JSONObject();
			Map<String, Object> object = convertirInfoboxAMap(jsonQuery
					.getJSONObject(i));

			if (object == null) {
				System.out.println("No se pudo cargar el elemento " + i
						+ " con los siguientes valores:\n\n");
				System.out.println(jsonQuery.getJSONObject(i));
				continue;
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			// Object obj = ;
			// Date fecha = (Date)obj;

			aux.put("fecha", formatter.format(object.get("fechainicia")));
			aux.put("fechaCreacion", formatter.format(new Date()));
			aux.put("titulo", titulo);
			aux.put("categoria", "deporte");
			aux.put("pais", "-1"); // convertirPais(object.getString("pageid"))
			aux.put("descripcionBreve", "");
			aux.put("link", ENCABEZADO_URL + titulo.replace(' ', '_'));
			aux.put("imagen", "");
			aux.put("ponderacion", 1);
			array.add(aux);
		}

		return array;
	}

	/**
	 * Método que convierte un infobox string a un json con sus datos
	 * principales
	 * 
	 * @param jsonObject
	 * @return
	 */
	private static Map<String, Object> convertirInfoboxAMap(
			JSONObject jsonObject) {
		// Capturo sólo el elemento del infobox completo
		String completo = jsonObject.getString("*");
		// System.out.println(completo);

		/*
		 * int indexInicio = completo.indexOf("{{"); int indexFin =
		 * completo.indexOf("}}"); String fichaTorneo =
		 * completo.substring(indexInicio, indexFin);
		 * System.out.println(fichaTorneo);
		 */

		// Detectar los atributos que se quieran rescatar
		// Año
		int indexAnio = completo.indexOf("año");
		String aux = completo.substring(indexAnio, completo.length());
		String anio = aux.substring(aux.indexOf("=") + 1, aux.indexOf("|"))
				.trim();

		if (anio.contains("y")) {
			anio = anio.substring(0, anio.indexOf('y')).trim();
		}

		// Fecha inicia
		int indexFechaInicia = completo.indexOf("fechainicia");
		String auxFe = null;

		try {
			auxFe = completo.substring(indexFechaInicia, completo.length());
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
		String fechainicia = auxFe.substring(auxFe.indexOf("=") + 1,
				auxFe.indexOf("|")).trim();

		Map<String, Object> ret = new HashMap<String, Object>();

		if (fechainicia.equals("")) {
			return null;
		}

		// Agregarlos a un JSONObject para poder devolverlo
		ret.put("fechainicia", crearFecha(fechainicia, anio));

		return ret;
	}

	/**
	 * Método que crea la fecha a partir de los strings recogidos en el evento
	 * de wikipedia, si es que puede
	 * 
	 * @param fechainicia
	 * @param anio
	 * @return
	 */
	private static Date crearFecha(String fechainicia, String anio) {
		int dia = -1;
		int mes = -1;
		int a = -1;
		String auxDia = null;

		try {
			a = Integer.parseInt(anio);
		} catch (NumberFormatException e) {
			return null;
		}

		try {
			auxDia = fechainicia.substring(0, fechainicia.indexOf("de"));
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		try {
			dia = Integer.parseInt(auxDia.replace('[', ' ').trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		String mesS = fechainicia.substring(fechainicia.indexOf("de") + 2)
				.trim();
		mes = convertirMes(mesS);

		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(a, mes, dia);
		return new Date(gc.getTimeInMillis());
	}

	private static int convertirMes(String mesS) {
		String array[] = { "enero", "febrero", "marzo", "abril", "mayo",
				"junio", "julio", "agosto", "septiembre", "octubre",
				"noviembre", "diciembre" };

		for (int i = 0; i < array.length; i++) {
			if (mesS.equalsIgnoreCase(array[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Método que convierte el valor de país de wikipedia al índice
	 * correspondiente de Memoria
	 * 
	 * @param string
	 * @return
	 */
	private static Object convertirPais(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}