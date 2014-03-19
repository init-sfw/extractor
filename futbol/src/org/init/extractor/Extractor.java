package org.init.extractor;

import java.net.MalformedURLException;

import org.init.extractor.wikipedia.Mapper;
import org.init.extractor.wikipedia.Plantilla;

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
		// Cargo el modelo de atributos de la plantilla seteada
		//Plantilla.cargarModeloAtributos();
		
		// Mapeo los atributos
		Mapper map = new Mapper();
		map.mapearla();
		
		// Cargo las páginas que correspondan a la plantilla seteada
		plantillaObjetivo.cargarPaginas(map);
		
		// Generar la salida y las estadísticas
		
	}

}
