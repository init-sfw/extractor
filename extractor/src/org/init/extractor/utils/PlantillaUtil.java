package org.init.extractor.utils;

import java.util.ArrayList;

import org.init.extractor.wikipedia.Pagina;
import org.init.extractor.wikipedia.Plantilla;
import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;
import org.init.extractor.wikipedia.eventos.EventoMemoria;

import net.sf.json.JSONObject;

/**
 * Clase de utilidades para trabajar con las plantillas de Wikipedia
 * 
 * @author fbobbio
 *
 */
public class PlantillaUtil {	
	
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
	public static String getAtributoPlantilla(String completo, String att) {
		try {
			int index = completo.indexOf(att);
			String aux = completo.substring(index, completo.length());
			String ret = aux.substring(aux.indexOf("=") + 1, aux.indexOf("\n")).trim();
			return ret;
		} catch (StringIndexOutOfBoundsException e) {
			return "sin datos";
		}
	}
	
	public static ArrayList<JSONObject> convertirPlantillaAJSON(Plantilla plantilla)
	{
		// Creo el jsonArray a devolver 
		ArrayList<JSONObject> array = new ArrayList<JSONObject>();

		// Itero el array y creo un nuevo json
		for (Pagina p : plantilla.getPaginasExitosas()) {
			JSONObject aux = new JSONObject();
			EventoMemoria e = p.getEvento();
			
			for (AtributoEventoMemoria att : e.getAtributos())
			{
				aux.put(att.getNombre(), att.getValorFinal());
			}
			array.add(aux);
		}		
		return array;		
	}

}
