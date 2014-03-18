package org.init.memoria.futbol;

import info.bliki.wiki.model.WikiModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.core.util.ServiceLocator;
import org.init.extractor.Constantes;

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

	public static void main(String[] args) {
		init("Ficha_de_torneo_de_fútbol");
	}

	public static void init(String plantilla) {
		
		//TODO: REFACTOR IMPORTANTE PLANTEADO AQUÍ 
		/*
		 * Hay que modificar el comportamiento de esta clase para que:
		 * 
		 * 	- Sea más atómica
		 * 	- Reciba cualquier plantilla de wikipedia
		 * 	- Funcione tanto por API como por fuente de datos pelados
		 * 	- Se puedan Mapear los atributos de la ficha a los campos de evento de memoria
		 * 	- En el punto anterior permitir componer muchos
		 * 	- Implementemos los test unitarios 
		 * 
		 */
		
		// Ejecuto el request de las páginas a wikipedia a través de su API HTML
		String cadenaPaginas = Constantes.QUERY_API_PLANTILLA + plantilla;
		String stringListaPaginas = requestGeneral(cadenaPaginas);
		JSONObject listaPaginas = convertStringToJSONObject(stringListaPaginas);

		System.out.println("\nListado de páginas encontradas con la plantilla " + plantilla + ":\n\n"
				+ listaPaginas.toString());

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
			String url = Constantes.ENCABEZADO_API_PAGINA + titulo.replace(' ', '_');

			String infobox = requestGeneral(url);
			ArrayList<JSONObject> aux = convertirInfoboxAMemoria(infobox,
					titulo, pageid);
			arrayInfoboxes.addAll(aux);
		}

		crearArchivoJSON(arrayInfoboxes);
		System.out.println("\n\n\n" + arrayInfoboxes
				+ "\n\nTotal de elementos convertidos: " + arrayInfoboxes.size()
				+ " de " + arrayProcesado.size());
	}


	/**
	 * Método que realiza el request HTTP que necesitemos según la cadena objetivo
	 * 
	 * @return string con la devolución de la consulta realizada
	 */
	public static String requestGeneral(String cadena) {
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

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		return sb.toString();
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
	private static ArrayList<JSONObject> convertirInfoboxAMemoria(
			String infobox, String titulo, String pageid) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(infobox);
		JSONArray jsonQuery = json.getJSONObject("query")
				.getJSONObject("pages").getJSONObject(pageid)
				.getJSONArray("revisions");

		// Creo el jsonArray a devolver luego
		ArrayList<JSONObject> array = new ArrayList<JSONObject>();

		// Itero el array y creo un nuevo json
		for (int i = 0; i < jsonQuery.size(); i++) {
			JSONObject aux = new JSONObject();
			Map<String, Object> object = convertirInfoboxAMap(
					jsonQuery.getJSONObject(i), titulo);

			if (object == null) { 
				//TODO: En estos casos declarar una excepción particular y lanzarla para capturar mejor el evento fallido
				System.out.println("No se pudo cargar el elemento " + i
						+ " con los siguientes valores:\n\n");
				System.out.println(jsonQuery.getJSONObject(i));
				continue;
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			try
			{
				aux.put("fecha", formatter.format(object.get("fechainicia")));				
			}
			catch (Exception e)
			{
				Object o = object.get("fechainicia");
				System.out.println(o);
			}
			aux.put("fechaCreacion", formatter.format(new Date()));
			aux.put("titulo", titulo);
			aux.put("categoria", "deporte");
			aux.put("pais", "-1"); // convertirPais(object.getString("pageid"))
			aux.put("descripcionBreve", object.get("descripcionbreve")
					.toString());
			aux.put("link", Constantes.ENCABEZADO_URL + titulo.replace(' ', '_'));
			aux.put("imagen", "");
			aux.put("ponderacion", 1);
			array.add(aux);

			System.out.println(object.get("descripcionbreve"));
		}

		return array;
	}

	/**
	 * Método que convierte un infobox string a un json con sus datos
	 * principales
	 * 
	 * @param jsonObject
	 * @param titulo
	 *            título del torneo
	 * @return
	 */
	private static Map<String, Object> convertirInfoboxAMap(
			JSONObject jsonObject, String titulo) {
		// Capturo sólo el elemento del infobox completo
		String completo = jsonObject.getString("*");
		// System.out.println(completo);

		/* Detectar los atributos que se quieran rescatar */
		// Año
		String anio = getAtributoPlantilla(completo, "año");

		if (anio.contains("y")) {
			anio = anio.substring(0, anio.indexOf('y')).trim();
		}
		
		// Fecha
		String fechainicia = getAtributoPlantilla(completo, "fechainicia");

		if (fechainicia.equals("")) {
			return null;
		}
		Date fecha = crearFecha(fechainicia, anio);
		if (fecha == null)
		{
			return null;
		}

		// Creo descripción breve, transformando la notación wiki a HTML
		String descripcionBreve = WikiModel.toHtml("Torneo: " + titulo + "\n"
				+ crearDescripcionBreve(completo)).replace("\n", "<br/>");

		// TODO: Activar esto cuando pueda resolver el parse de MediaWiki a HTML correcto
		// Creo descripción breve, transformando la notación wiki a HTML
		/*String descripcionBreve = parseWikiTextByLanguageToHTML(
				"Torneo: " + titulo + "\n" + crearDescripcionBreve(completo))
				.replace("\n", "<br/>");
		
		descripcionBreve = limpiarHTML(descripcionBreve);
		descripcionBreve = reemplazarLinks(descripcionBreve);
		
		System.out.println(descripcionBreve);*/

		// Creo el map a retornar
		Map<String, Object> ret = new HashMap<String, Object>();		

		// Agregarlos a un JSONObject para poder devolverlo
		ret.put("fechainicia", fecha);
		ret.put("descripcionbreve", descripcionBreve);

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
	 * Método que crea la descripción breve para memoria
	 * 
	 * @param completo
	 * @return
	 */
	private static String crearDescripcionBreve(String completo) {
		String ret;

		// Sede
		String sede = "\nSede: " + getAtributoPlantilla(completo, "pais");
		// Campeón
		String campeon = "\nCampeón: "
				+ getAtributoPlantilla(completo, "campeón");
		// SubCampeón
		String subcampeon = "\nSubcampeón: "
				+ getAtributoPlantilla(completo, "subcampeón");
		// Goleador
		String goleador = "\nGoleador: "
				+ getAtributoPlantilla(completo, "goleador");
		// Goles totales
		String goles = "\nGoles totales: "
				+ getAtributoPlantilla(completo, "goles");
		
		// Reemplazo los valores que pueden molestar
		ret = (sede + campeon + subcampeon + goleador + goles).replace("selb|", "")
				.replace("Selb|", "")
				.replace("sel|", "")
				.replace("bandera|", "")
				.replace("bandera2|", "");

		return ret;
	}

	/**
	 * Método que recoge un atributo del infobox de la plantilla y devuelve su
	 * valor
	 * 
	 * @param completo
	 *            el string completo del contenido de wikipedia
	 * @param att
	 *            el nombre del atributo que se desea obtener
	 * @return el valor del atributo buscado
	 */
	private static String getAtributoPlantilla(String completo, String att) {
		try {
			int index = completo.indexOf(att);
			String aux = completo.substring(index, completo.length());
			String ret = aux.substring(aux.indexOf("=") + 1, aux.indexOf("\n")).trim();
			return ret;
		} catch (StringIndexOutOfBoundsException e) {
			return "sin datos";
		}
	}

	/**
	 * Método que parsea un string según un markupLanguaje a HTML
	 * 
	 * @param wikiText
	 * @return
	 */
	public static String parseWikiTextByLanguageToHTML(String wikiText) {
		MarkupLanguage language = ServiceLocator.getInstance()
				.getMarkupLanguage("MediaWiki");
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		MarkupParser parser = new MarkupParser(language, builder);
		parser.parse(wikiText);
		return writer.toString();
	}

	/** 
	 * Método que limpia los tags HTML del string parseado desde el formato wikimedia
	 * @param descripcionBreve
	 * @return
	 */
	private static String limpiarHTML(String descripcionBreve) {
		return descripcionBreve.substring(descripcionBreve.indexOf("<body>") + 6, descripcionBreve.indexOf("</body>"));
	}

	/**
	 * Método que completa como se debe el href de los links generados desde wikipedia
	 * @param descripcionBreve
	 * @return
	 */
	private static String reemplazarLinks(String descripcionBreve) {
		//TODO: definir bien este método
		throw new RuntimeException("Método sin implementar reemplazarLinks(String)");
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