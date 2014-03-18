package org.init.memoria.futbol.eventos;

import java.util.HashMap;
import java.util.Map;

/**
 * Colección de tipo mapa que contiene a los atributos extraídos y la
 * posibilidad de definir y convertir una plantilla de muestra acorde en el
 * evento final de memoria
 * 
 * @author fbobbio
 * 
 */
public class MapaAtributosMapeados extends HashMap<String, AtributoExtranjero> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Una plantilla de mapeo múltiple para decidir cómo todos los atributos
	 * contenidos en el mapa deben adecuarse a un atributo de Memoria, con
	 * referencias a las key usadas entre corchetes
	 * 
	 * Ejemplo de plantilla:
	 * "El evento ocurrió en la fecha {fecha} en la ciudad {ciudad}"
	 * 
	 * Donde {fecha} y {ciudad} serán reemplazados por los valores que
	 * correspondan dentro de este mapa
	 */
	private String plantillaMapeo;

	/**
	 * Obtengo el original de la plantilla de mapeo múltiple.
	 * 
	 * Se utiliza para decidir cómo todos los atributos contenidos en el mapa
	 * deben adecuarse a un atributo de Memoria, con referencias a las key
	 * usadas entre corchetes
	 * 
	 * Ejemplo de plantilla:
	 * "El evento ocurrió en la fecha {fecha} en la ciudad {ciudad}"
	 * 
	 * Donde {fecha} y {ciudad} serán reemplazados por los valores que
	 * correspondan dentro de este mapa
	 */
	public String getPlantillaMapeo() {
		return plantillaMapeo;
	}

	/**
	 * Setea la plantilla de mapeo múltiple para definir cómo se mapearán muchos
	 * atributos extraídos a un solo atributo de Memoria.
	 * 
	 * @param plantillaMapeo
	 */
	public void setPlantillaMapeo(String plantillaMapeo) {
		this.plantillaMapeo = plantillaMapeo;
	}

	/**
	 * Método que devuelve el correcto String convertido a partir de la
	 * plantilla
	 */
	@Override
	public String toString() {
		String ret = plantillaMapeo;
		if (plantillaMapeo != null) {
			for (Map.Entry<String, AtributoExtranjero> at : this.entrySet()) {
				String key = at.getKey();
				String value = at.getValue().toString();
				ret = ret.replace("{" + key + "}", value);
			}
		} else {
			for (Map.Entry<String, AtributoExtranjero> at : this.entrySet()) {
				ret = at.getValue().toString();
			}
		}
		return ret;
	}

}
