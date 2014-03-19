package org.init.extractor.utils;

import info.bliki.wiki.model.WikiModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class ArrangeProcessor {

	/**
	 * Método que procesa un arrange determinado sobre el valor recibido por
	 * parámetro
	 * 
	 * @param arrange
	 * @param valor
	 * @return el valor procesado con los arranges
	 * 
	 */
	public static String procesarArrange(ArrangeEnum arrange, String valor) throws StringIndexOutOfBoundsException, NumberFormatException {
		switch (arrange) {
		case FECHA:
			return procesarFecha(valor);
		case DESCRIPCION_BREVE:
			return procesarDescripcion(valor);
		case URL:
			return procesarURL(valor);
		default:
			return valor;
		}
	}

	private static String procesarFecha(String valor) throws StringIndexOutOfBoundsException, NumberFormatException {
		return crearFecha(valor);
	}

	private static String procesarDescripcion(String valor) {
		// Quito algunos valores wiki que no me interesan
		valor = valor.replace("selb|", "").replace("Selb|", "")
				.replace("sel|", "").replace("bandera|", "")
				.replace("bandera2|", "");
		// Transformo la notación wiki a HTML
		valor = WikiModel.toHtml(valor).replace("\n", "<br/>");
		return valor;
	}

	private static String procesarURL(String valor) {
		return valor.replace(" ", "_");
	}

	/**
	 * Método que crea la fecha a partir de los strings recogidos en el evento
	 * de wikipedia, si es que puede
	 * 
	 * @param fecha
	 * 
	 * @throws StringIndexOutOfBoundsException
	 * @throws NumberFormatException
	 */
	private static String crearFecha(String fecha) throws StringIndexOutOfBoundsException, NumberFormatException {
		int dia = -1;
		int mes = -1;
		int a = -1;
		String auxDia = null;
		String auxAnio;

		auxAnio = fecha.substring(fecha.indexOf("/") + 1).trim();
		if (auxAnio.contains("y"))
		{
			auxAnio = auxAnio.substring(0,auxAnio.indexOf("y")).trim();
		}
		a = Integer.parseInt(auxAnio);
		auxDia = fecha.substring(0, fecha.indexOf("de"));
		dia = Integer.parseInt(auxDia.replace('[', ' ').trim());

		String mesS = fecha.substring(fecha.indexOf("de") + 2,fecha.indexOf("/")).trim();
		mes = convertirMes(mesS);

		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(a, mes, dia);
		Date d = new Date(gc.getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		return formatter.format(d);
	}

	/**
	 * Convierte el string de mes a un valor numérico
	 * 
	 * @param mesS
	 * @return
	 */
	private static int convertirMes(String mesS) {
		String array[] = { "enero", "febrero", "marzo", "abril", "mayo",
				"junio", "julio", "agosto", "septiembre", "octubre",
				"noviembre", "diciembre" };

		for (int i = 0; i < array.length; i++) {
			if (mesS.equalsIgnoreCase(array[i])) {
				return i;
			}
		}
		return -1;
	}

}
