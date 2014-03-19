package org.init.extractor.wikipedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.init.extractor.Constantes;
import org.init.extractor.excepciones.ImposibleMapearEventoException;
import org.init.extractor.utils.ArrangeEnum;
import org.init.extractor.utils.PlantillaUtil;
import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;
import org.init.extractor.wikipedia.eventos.AtributoExtranjero;
import org.init.extractor.wikipedia.eventos.EventoMemoria;
import org.init.extractor.wikipedia.eventos.MapaAtributosMapeados;

/**
 * Clae que resuelve el mapeo de los atributos de memoria a los atributos de las plantillas de wikipedia
 * 
 * @author fbobbio
 *
 */
public class Mapper {
	
	private ArrayList<AtributoEventoMemoria> atributosMemoria;
	
	private EventoMemoria eventoModelo;
	
	public Mapper()
	{
		atributosMemoria = new ArrayList<AtributoEventoMemoria>();
		eventoModelo = new EventoMemoria();
		eventoModelo.setAtributos(atributosMemoria);
	}
	
	/**
	 * Método que mapea el valor de un atributo de memoria a una serie de valores de atributos de la plantilla de wikipedia
	 * 
	 * @param atMemoria
	 * @param atExt
	 */
	public void map(AtributoEventoMemoria atMemoria, MapaAtributosMapeados atExt)
	{
		atMemoria.setListaMapeos(atExt);
		atributosMemoria.add(atMemoria);
	}
	
	/**
	 * Método que crea el mapeo modelo de la plantilla contra los atributos del evento Memoria
	 */
	public void mapearla()
	{
		String [] attFecha = {"fechainicia", "año"};
		String plantillaFecha = "{fechainicia}/{año}";
		List<ArrangeEnum> arrangesFecha = new ArrayList<ArrangeEnum>(1);
		arrangesFecha.add(ArrangeEnum.FECHA);
		mapear("fechaCreacion", "descripcion", null); //TODO: debería ser valor creado acá
		mapear("fecha", attFecha, plantillaFecha, arrangesFecha);
		mapear("titulo", "torneo", null);
		mapear("categoria", "categoria", null); //TODO: debería ser valor fijo
		mapear("pais", "pais", null);
		List<ArrangeEnum> arrangesLink = new ArrayList<ArrangeEnum>(1);
		arrangesLink.add(ArrangeEnum.URL);
		mapear("link", Constantes.ENCABEZADO_URL + "torneo", arrangesLink); 
		mapear("imagen", "imagen", null);
		mapear("ponderacion", "ponderacion", null); //TODO: debería ser valor fijo
		String atts[] = {"pais","campeón","subcampeón","goleador","goles"};
		String plantillaDescripcion = "\nSede: {pais} \nCampeón: {campeón}\nSubcampeón: {subcampeón}\nGoleador: {goleador}\nGoles totales: {goles}";
		List<ArrangeEnum> arrangesDesc = new ArrayList<ArrangeEnum>(1);
		arrangesDesc.add(ArrangeEnum.DESCRIPCION_BREVE);
		mapear("descripcionBreve", atts, plantillaDescripcion, arrangesDesc);
		
	}

	/**
	 * Método que mapea un atributo de Memoria a un atributo de los datos extraídos
	 * considerando una serie de arranges que procesarán el valor para que sea el adecuado
	 * 
	 * @param attMemoria
	 * @param tipoMemoria
	 * @param attFicha
	 * @param arranges
	 */
	private void mapear(String attMemoria, String attFicha, List<ArrangeEnum> arranges)
	{
		AtributoEventoMemoria att = new AtributoEventoMemoria(attMemoria);
		att.setArranges(arranges);
		AtributoExtranjero ext = new AtributoExtranjero(attFicha);
		MapaAtributosMapeados map = new MapaAtributosMapeados();
		map.put(attFicha, ext);
		map(att,map);		
	}

	/**
	 * Método que mapea un atributo de Memoria a una serie de atributos de los datos extraídos
	 * considerando una serie de arranges que procesarán el valor para que sea el adecuado y una 
	 * plantilla que defina la concatenación que necesiten múltiples atributos
	 * 
	 * @param attMemoria
	 * @param atts
	 * @param plantilla
	 * @param arranges
	 * 
	 */	 
	private void mapear(String attMemoria, String[] atts, String plantilla,List<ArrangeEnum> arranges) {
		AtributoEventoMemoria att = new AtributoEventoMemoria(attMemoria);
		att.setArranges(arranges);
		MapaAtributosMapeados map = new MapaAtributosMapeados();
		map.setPlantillaMapeo(plantilla);
		for (String attFicha : atts)
		{
			AtributoExtranjero ext = new AtributoExtranjero(attFicha);
			map.put(attFicha, ext);
		}
		map(att,map);		
	}

	public EventoMemoria getEventoModelo() {
		return eventoModelo;
	}

	public void setEventoModelo(EventoMemoria evento) {
		this.eventoModelo = evento;
	}
}
