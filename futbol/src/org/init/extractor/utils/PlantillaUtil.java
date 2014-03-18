package org.init.extractor.utils;

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

}
