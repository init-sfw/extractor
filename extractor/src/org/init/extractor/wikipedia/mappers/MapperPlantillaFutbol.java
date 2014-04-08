package org.init.extractor.wikipedia.mappers;

import java.util.ArrayList;
import java.util.List;

import org.init.extractor.utils.ArrangeEnum;
import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;

/**
 * Clase de mapeo que se encarga de procesar la plantilla "Ficha_de_torneo_de_fútbol" de Wikipedia a formato
 * de evento Memoria
 * 
 * @author fbobbio
 *
 */
public class MapperPlantillaFutbol extends Mapper {


	@Override
	protected void mapearFecha(AtributoEventoMemoria att) {
		String [] attFecha = {"fechainicia", "año"};
		String plantillaFecha = "{fechainicia}/{año}";
		List<ArrangeEnum> arrangesFecha = new ArrayList<ArrangeEnum>(1);
		arrangesFecha.add(ArrangeEnum.FECHA);
		super.ejecutarMapeo(att, attFecha, plantillaFecha, arrangesFecha);
	}

	@Override
	protected void mapearFechaCreacion(AtributoEventoMemoria att) {
		List<ArrangeEnum> arrangeFechaActual = new ArrayList<ArrangeEnum>();
		arrangeFechaActual.add(ArrangeEnum.FECHA_ACTUAL);
		ejecutarMapeo(att, "fechaCreacion", arrangeFechaActual);
	}
	
	@Override
	protected void mapearTitulo(AtributoEventoMemoria att) {
		List<ArrangeEnum> arrangeTitulo = new ArrayList<ArrangeEnum>();
		arrangeTitulo.add(ArrangeEnum.TITULO_PAGINA);
		ejecutarMapeo(att, "torneo", arrangeTitulo); //TODO: Arreglar
	}

	@Override
	protected void mapearCategoria(AtributoEventoMemoria att) {
		List<ArrangeEnum> arrangeCategoria = new ArrayList<ArrangeEnum>();
		arrangeCategoria.add(ArrangeEnum.CATEGORIA);
		ejecutarMapeo(att, "categoria", arrangeCategoria);
	}

	@Override
	protected void mapearPais(AtributoEventoMemoria att) {
		List<ArrangeEnum> arrangesPais = new ArrayList<ArrangeEnum>(1);
		arrangesPais.add(ArrangeEnum.PAIS);
		ejecutarMapeo(att, "pais", arrangesPais);
	}

	@Override
	protected void mapearDescripcionBreve(AtributoEventoMemoria att) {
		String atts[] = {"pais","campeón","subcampeón","goleador","goles"};
		String plantillaDescripcion = "\nSede: {pais} \nCampeón: {campeón}\nSubcampeón: {subcampeón}\nGoleador: {goleador}\nGoles totales: {goles}";
		List<ArrangeEnum> arrangesDesc = new ArrayList<ArrangeEnum>(1);
		arrangesDesc.add(ArrangeEnum.DESCRIPCION_BREVE);
		ejecutarMapeo(att, atts, plantillaDescripcion, arrangesDesc);	
	}

	@Override
	protected void mapearLink(AtributoEventoMemoria att) {
		List<ArrangeEnum> arrangesLink = new ArrayList<ArrangeEnum>(1);
		arrangesLink.add(ArrangeEnum.LINK);
		ejecutarMapeo(att, "link", arrangesLink);  //TODO: Arreglar
	}

	@Override
	protected void mapearImagen(AtributoEventoMemoria att) {
		ejecutarMapeo(att, "imagen", null); //TODO: Arreglar
	}

	@Override
	protected void mapearPonderacion(AtributoEventoMemoria att) {
		//TODO: en este caso los arranges quizás puedan evitarse seteando el valor directamente
		List<ArrangeEnum> arrangesPonderacion = new ArrayList<ArrangeEnum>(1);
		arrangesPonderacion.add(ArrangeEnum.PONDERACION);
		ejecutarMapeo(att, "ponderacion", arrangesPonderacion);
	}

}
