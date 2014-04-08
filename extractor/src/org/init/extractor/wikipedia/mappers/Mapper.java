package org.init.extractor.wikipedia.mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.init.extractor.Constantes;
import org.init.extractor.excepciones.ImposibleMapearEventoException;
import org.init.extractor.utils.ArrangeEnum;
import org.init.extractor.utils.AtributosMemoriaEnum;
import org.init.extractor.utils.PlantillaUtil;
import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;
import org.init.extractor.wikipedia.eventos.AtributoExtranjero;
import org.init.extractor.wikipedia.eventos.EventoMemoria;
import org.init.extractor.wikipedia.eventos.MapaAtributosMapeados;

/**
 * Clase abstracta que define el mapeo de los atributos de memoria a los atributos de 
 * las plantillas de wikipedia
 * 
 * @author fbobbio
 *
 */
public abstract class Mapper {
	
	public Mapper()
	{
	}
	
	/**
	 * Método que crea el mapeo modelo de la plantilla contra los atributos del evento Memoria
	 */
	public void mapearEvento(EventoMemoria evt)
	{
		crearAtributos(evt);
		
		// Recorro los atributos memoria y voy creando el mapping de cada uno
		for (AtributoEventoMemoria att : evt.getAtributos())
		{
			// Mapeo el atributo
			mapear(att);			
		}		
	}

	/**
	 * Método que crea los atributos necesarios para Memoria
	 * 
	 * @param evt
	 */
	private void crearAtributos(EventoMemoria evt) {
		for (AtributosMemoriaEnum tipo : AtributosMemoriaEnum.values())
		{
			AtributoEventoMemoria atributo = new AtributoEventoMemoria(tipo, evt);
			evt.addAtributo(atributo);
		}
	}

	/**
	 * Método que mapea un atributo memoria con los que le correspondan según el tipo
	 * 
	 * @param att
	 */
	private void mapear(AtributoEventoMemoria att) {
		switch (att.getTipo())
		{
		case FECHA:
			mapearFecha(att);
			break;
		case FECHA_CREACION:
			mapearFechaCreacion(att);
			break;
		case TITULO:
			mapearTitulo(att);
			break;
		case CATEGORIA:
			mapearCategoria(att);
			break;
		case PAIS:
			mapearPais(att);
			break;
		case DESCRIPCION_BREVE:
			mapearDescripcionBreve(att);
			break;
		case LINK:
			mapearLink(att);
			break;
		case IMAGEN:
			mapearImagen(att);
			break;
		case PONDERACION:
			mapearPonderacion(att);
			break;
		default:
			break;
				
		}
	}

	protected abstract void mapearFecha(AtributoEventoMemoria att);

	protected abstract void mapearFechaCreacion(AtributoEventoMemoria att);
	
	protected abstract void mapearTitulo(AtributoEventoMemoria att);

	protected abstract void mapearCategoria(AtributoEventoMemoria att);

	protected abstract void mapearPais(AtributoEventoMemoria att);

	protected abstract void mapearDescripcionBreve(AtributoEventoMemoria att);

	protected abstract void mapearLink(AtributoEventoMemoria att);

	protected abstract void mapearImagen(AtributoEventoMemoria att);

	protected abstract void mapearPonderacion(AtributoEventoMemoria att);

	/**
	 * Método que mapea un atributo de Memoria a un atributo de los datos extraídos
	 * considerando una serie de arranges que procesarán el valor para que sea el adecuado
	 * 
	 * @param attMemoria
	 * @param attFicha
	 * @param arranges
	 */
	protected void ejecutarMapeo(AtributoEventoMemoria attMemoria, String attFicha, List<ArrangeEnum> arranges)
	{
		attMemoria.setArranges(arranges);
		AtributoExtranjero ext = new AtributoExtranjero(attFicha);
		MapaAtributosMapeados map = new MapaAtributosMapeados();
		map.put(attFicha, ext);
		map(attMemoria,map);		
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
	protected void ejecutarMapeo(AtributoEventoMemoria attMemoria, String[] atts, String plantilla,List<ArrangeEnum> arranges) {
		attMemoria.setArranges(arranges);
		MapaAtributosMapeados map = new MapaAtributosMapeados();
		map.setPlantillaMapeo(plantilla);
		for (String attFicha : atts)
		{
			AtributoExtranjero ext = new AtributoExtranjero(attFicha);
			map.put(attFicha, ext);
		}
		map(attMemoria,map);		
	}
	
	/**
	 * Método que mapea el valor de un atributo de memoria a una serie de valores de atributos de la plantilla de wikipedia
	 * 
	 * @param atMemoria
	 * @param atExt
	 */
	private void map(AtributoEventoMemoria atMemoria, MapaAtributosMapeados atExt)
	{
		atMemoria.setListaMapeos(atExt);
	}
}
