package org.init.extractor.wikipedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.init.extractor.excepciones.ImposibleMapearEventoException;
import org.init.extractor.utils.ArrangeEnum;
import org.init.extractor.utils.PlantillaUtil;
import org.init.memoria.futbol.eventos.AtributoEventoMemoria;
import org.init.memoria.futbol.eventos.AtributoExtranjero;
import org.init.memoria.futbol.eventos.EventoMemoria;
import org.init.memoria.futbol.eventos.MapaAtributosMapeados;

/**
 * Clae que resuelve el mapeo de los atributos de memoria a los atributos de las plantillas de wikipedia
 * 
 * @author fbobbio
 *
 */
public class Mapper {
	
	private ArrayList<AtributoEventoMemoria> atributosMemoria;
	
	private EventoMemoria evento;
	
	public Mapper()
	{
		atributosMemoria = new ArrayList<AtributoEventoMemoria>();
		evento = new EventoMemoria();
		evento.setAtributos(atributosMemoria);
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
		mapear("fechaCreacion", Date.class, attFecha, plantillaFecha, arrangesFecha);
		mapear("titulo", String.class, "titulo", null);
		mapear("categoria", String.class, "categoria", null);
		mapear("pais", String.class, "pais", null);
		mapear("link", String.class, "link", null);
		mapear("imagen", String.class, "imagen", null);
		mapear("ponderacion", String.class, "ponderacion", null);
		String atts[] = {"pais","campeón","subcampeón","goleador","goles"};
		String plantillaDescripcion = "\nSede: {pais} \nCampeón: {campeón}\nSubcampeón: {subcampeón}\nGoleador: {goleador}\nGoles totales: {goles}";
		List<ArrangeEnum> arrangesDesc = new ArrayList<ArrangeEnum>(1);
		arrangesDesc.add(ArrangeEnum.DESCRIPCION_BREVE);
		mapear("descripcionBreve", String.class, atts, plantillaDescripcion, arrangesDesc);
		
	}

	private void mapear(String attMemoria, Object tipoMemoria, String attFicha, List<ArrangeEnum> arranges)
	{
		AtributoEventoMemoria att = new AtributoEventoMemoria(attMemoria, tipoMemoria);
		att.setArranges(arranges);
		AtributoExtranjero ext = new AtributoExtranjero(attFicha);
		MapaAtributosMapeados map = new MapaAtributosMapeados();
		map.put(attFicha, ext);
		map(att,map);		
	}
	
	private void mapear(String attMemoria, Object tipoMemoria, String[] atts, String plantilla,List<ArrangeEnum> arranges) {
		AtributoEventoMemoria att = new AtributoEventoMemoria(attMemoria, tipoMemoria);
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
	
	/**
	 * Método que levanta los valores de los mapeos desde el infobox y los devuelve en un objeto EventoMemoria cargado según mapeo
	 * 
	 * @param p la página de la que se quiere mapear el contenido en string del infobox
	 * 
	 */
	public EventoMemoria levantarMapeo(Pagina p)
	{
		// Ejecuto constructor copia para que tome los mapeos
		EventoMemoria evt = new EventoMemoria(evento,p);
		
		for (AtributoEventoMemoria att : evt.getAtributos())
		{
			levantarValorAtributo(att, p.getContenidoPlantilla());
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

	public EventoMemoria getEvento() {
		return evento;
	}

	public void setEvento(EventoMemoria evento) {
		this.evento = evento;
	}
}
