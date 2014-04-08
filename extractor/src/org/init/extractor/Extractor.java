package org.init.extractor;

import java.net.MalformedURLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.init.extractor.utils.FileUtil;
import org.init.extractor.utils.PlantillaUtil;
import org.init.extractor.wikipedia.Plantilla;
import org.init.extractor.wikipedia.mappers.Mapper;

/** 
 * Clase central encargada de comandar las tareas de todo el aplicativo para generar fuentes de datos
 * exportadas a partir de otras.
 * 
 * @author fbobbio
 *
 */
public class Extractor {
	
	private Plantilla plantillaObjetivo;
	
	public Extractor (Plantilla plantilla)
	{
		this.plantillaObjetivo = plantilla;
	}
	
	/**
	 * Método central encargado de la inicialización de todas las funcionalidades que realizan la extracción
	 * y la publican
	 * @throws MalformedURLException 
	 */
	public void init() throws MalformedURLException
	{
		// Cargo las páginas que correspondan a la plantilla seteada
		plantillaObjetivo.cargarPaginas();

		// Array con todos los infoboxes de las páginas que se pedirán
		JSONArray arrayInfoboxes = new JSONArray();		
		// Generar la salida en formato JSON
		ArrayList<JSONObject> jsonFinal = PlantillaUtil.convertirPlantillaAJSON(plantillaObjetivo);
		arrayInfoboxes.addAll(jsonFinal);
		
		// Genero el archivo JSON
		FileUtil.crearArchivoJSON("datos-deportes.json", arrayInfoboxes);
		
		// Estadísticas
		info();
	}

	private void info() {
		System.out.println("\nSe cargaron " + plantillaObjetivo.getPaginasExitosas().size()
				+ " datos de páginas de un total de " + (plantillaObjetivo.getPaginasExitosas().size() + plantillaObjetivo.getPaginasFallidas().size()));
		System.out.println("Fallaron: " + plantillaObjetivo.getPaginasFallidas().size() + " páginas");
		double porc = plantillaObjetivo.getPaginasExitosas().size() * 100 / (plantillaObjetivo.getPaginasExitosas().size() + plantillaObjetivo.getPaginasFallidas().size());
		System.out.println("Porcentaje de efectividad de la fuente de datos con el mappeo: " + porc + "%");
		
	}
}
