package org.init.extractor.wikipedia;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.init.extractor.Constantes;
import org.init.extractor.excepciones.ImposibleMapearEventoException;
import org.init.extractor.utils.PlantillaUtil;
import org.init.extractor.utils.Request;
import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;
import org.init.extractor.wikipedia.eventos.AtributoExtranjero;
import org.init.extractor.wikipedia.eventos.EventoMemoria;
import org.init.extractor.wikipedia.eventos.MapaAtributosMapeados;

/**
 * Clase que representa a una página particular de Wikipedia, la cual puede
 * estar asociada a plantillas y atributos de estas
 * 
 * @author fede
 * 
 */
public class Pagina {

	private String nombre;
	private String idPagina;
	private URL url;
	private URL urlAPI;
	private String contenidoCompleto;
	private String contenidoPlantilla;
	private EventoMemoria evento;
	private Plantilla plantillaPadre;

	/**
	 * Constructor de Página que recibe el nombre y realizará la consulta que le
	 * traiga el contenido
	 * 
	 * @param nombre
	 *            nombre que se le dará a la página
	 * @throws MalformedURLException
	 */
	public Pagina(String nombre, String idPagina, Plantilla padre)
			throws MalformedURLException {
		this.nombre = nombre;
		this.idPagina = idPagina;
		this.plantillaPadre = padre;
		this.url = new URL(Constantes.ENCABEZADO_URL + nombre.replace(" ", "_"));
		this.urlAPI = new URL(Constantes.ENCABEZADO_API_PAGINA
				+ nombre.replace(" ", "_"));
		this.contenidoCompleto = null;
		this.contenidoPlantilla = null;
	}

	/**
	 * Método que busca el contenido de la página a través de la API
	 */
	public void generarContenido() {
		// Levanto de la API la página completa
		this.contenidoCompleto = Request.requestGeneral(urlAPI.toString());

		JSONObject json = (JSONObject) JSONSerializer.toJSON(this
				.getContenido());
		JSONObject jsonPlantilla = json.getJSONObject("query")
				.getJSONObject("pages").getJSONObject(this.getIdPagina())
				.getJSONArray("revisions").getJSONObject(0);

		// Capturo sólo el elemento del infobox completo
		this.contenidoPlantilla = jsonPlantilla.getString("*");
	}

	/**
	 * Método que carga los atributos del template de wikipedia a la página,
	 * respetando el formato de mapeo establecido
	 */
	public void cargarAtributos(Mapper mapper) {
		EventoMemoria evt = levantarMapeo(mapper);
		this.evento = evt;
	}
	
	/**
	 * Método que levanta los valores de los mapeos desde el infobox y los devuelve en un objeto EventoMemoria cargado según mapeo
	 * 
	 * @param mapper
	 * 
	 */
	public EventoMemoria levantarMapeo(Mapper mapper)
	{		
		// Creo el evento para Memoria
		EventoMemoria evt = new EventoMemoria(this);
		
		// Mapeo todos los atributos
		mapper.mapearEvento(evt);
		
		for (AtributoEventoMemoria att : evt.getAtributos())
		{
			levantarValorAtributo(att, this.contenidoPlantilla);
			// Proceso atributos según los arranges necesarios
			try {
				att.procesar();
			} catch (ImposibleMapearEventoException e) {
				System.out.println(e.getMessage() + "\n" + e.getCause());
				return null;
			}
		}
		
		return evt;		
	}

	/**
	 * Método que levanta el valor de un atributo sin procesarlo
	 * @param att
	 */
	private void levantarValorAtributo(AtributoEventoMemoria att, String infobox) {		
		for (Map.Entry<String, AtributoExtranjero> entry : att.getListaMapeos().entrySet())
		{
			AtributoExtranjero atExt = entry.getValue();
			String valorAt = PlantillaUtil.getAtributoPlantilla(infobox, atExt.getNombre());
			atExt.setValor(valorAt);
		}
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

	public URL getUrlAPI() {
		return urlAPI;
	}

	public void setUrlAPI(URL urlAPI) {
		this.urlAPI = urlAPI;
	}

	public String getIdPagina() {
		return idPagina;
	}

	public void setIdPagina(String idPagina) {
		this.idPagina = idPagina;
	}

	public String getContenido() {
		return contenidoCompleto;
	}

	public void setContenido(String contenido) {
		this.contenidoCompleto = contenido;
	}

	public String getContenidoCompleto() {
		return contenidoCompleto;
	}

	public void setContenidoCompleto(String contenidoCompleto) {
		this.contenidoCompleto = contenidoCompleto;
	}

	public String getContenidoPlantilla() {
		return contenidoPlantilla;
	}

	public void setContenidoPlantilla(String contenidoPlantilla) {
		this.contenidoPlantilla = contenidoPlantilla;
	}

	public Plantilla getPlantillaPadre() {
		return plantillaPadre;
	}

	public void setPlantillaPadre(Plantilla plantillaPadre) {
		this.plantillaPadre = plantillaPadre;
	}

	public EventoMemoria getEvento() {
		return evento;
	}

	public void setEvento(EventoMemoria evento) {
		this.evento = evento;
	}
}
