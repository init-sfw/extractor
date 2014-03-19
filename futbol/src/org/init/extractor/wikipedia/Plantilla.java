package org.init.extractor.wikipedia;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.init.extractor.Constantes;
import org.init.extractor.utils.Request;
import org.init.memoria.futbol.eventos.MapaAtributosMapeados;

/**
 * Clase que representa originalmente una plantilla de wikipedia para contener
 * sus valores y otorgar la funcionalidad que le corresponde en cuanto a su url,
 * sus atributos y las páginas que la implementan.
 * 
 * @see <a
 *      href="http://es.wikipedia.org/wiki/Ayuda:Plantillas">Wikipedia:Plantilla</a>
 * 
 * @author fbobbio
 * 
 */
public class Plantilla {

	private String nombre;
	private URL url;
	List<Pagina> paginasExitosas;
	List<Pagina> paginasFallidas;

	public Plantilla(String nombre) throws MalformedURLException {
		this.nombre = nombre;
		this.url = new URL(Constantes.QUERY_API_PLANTILLA + this.nombre);
		paginasExitosas = new ArrayList<Pagina>();
		paginasFallidas = new ArrayList<Pagina>();
	}

	/**
	 * Método que carga todas las páginas que correspondan a la plantilla
	 * seteada con su infobox correspondiente, sus atributos y toda la
	 * información de la página.
	 * 
	 * @throws MalformedURLException
	 */
	public void cargarPaginas(Mapper mapper) throws MalformedURLException {
		// Ejecuto el request de las páginas a wikipedia a través de su API HTML
		String stringListaPaginas = Request.requestGeneral(this.getUrl()
				.toString());
		JSONObject listaPaginas = convertStringToJSONObject(stringListaPaginas);

		System.out.println("\nListado de páginas encontradas con la plantilla "
				+ this.nombre + ":\n\n" + listaPaginas.toString());

		// Tomo el array del json prosesado con la lista de las páginas
		JSONArray arrayProcesado = listaPaginas.getJSONArray("array");

		// Recorro el arrayProcesado, busco cada una de sus páginas y traigo el
		// resultado en json que guardo en arrayInfoboxes
		for (int i = 0; i < 74; i++) { // TODO: arrayProcesado.size()
			JSONObject object = (JSONObject) arrayProcesado.get(i);
			String titulo = object.get("title").toString();
			String pageid = object.get("pageid").toString();

			Pagina p = new Pagina(titulo, pageid, this);
			p.generarContenido();
			p.cargarAtributos(mapper);
			if (p.isExitosa()) {
				paginasExitosas.add(p);
			}
			else {
				paginasFallidas.add(p);
			}
		}

		System.out.println("\nSe cargaron " + paginasExitosas.size()
				+ " datos de páginas de un total de " + (paginasExitosas.size() + paginasFallidas.size()));
		System.out.println("Fallaron: " + paginasFallidas.size() + " páginas");
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public List<Pagina> getPaginas() {
		return paginasExitosas;
	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginasExitosas = paginas;
	}
}
