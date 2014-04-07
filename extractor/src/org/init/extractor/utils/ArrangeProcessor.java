package org.init.extractor.utils;

import info.bliki.wiki.model.WikiModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.init.extractor.Constantes;
import org.init.extractor.excepciones.ImposibleMapearEventoException;

public class ArrangeProcessor {
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Método que procesa un arrange determinado sobre el valor recibido por
	 * parámetro
	 * 
	 * @param arrange
	 * @param valor
	 * @return el valor procesado con los arranges
	 * @throws ImposibleMapearEventoException 
	 * 
	 */
	public static String procesarArrange(ArrangeEnum arrange, String valor) throws StringIndexOutOfBoundsException, NumberFormatException, ImposibleMapearEventoException {
		switch (arrange) {
		case FECHA:
			return procesarFecha(valor);
		case DESCRIPCION_BREVE:
			return procesarDescripcion(valor);
		case URL:
			return procesarURL(valor);
		case FECHA_ACTUAL:
			return procesarFechaActual();
		case CATEGORIA:
			return procesarCategoria();
		case PONDERACION:
			return procesarPonderacion();
		case TITULO_PAGINA:
			return procesarTitulo(valor);
		case LINK:
			return procesarLink(valor);
		case PAIS:
			return procesarPais(valor);
		default:
			return valor;
		}
	}

	private static String procesarFecha(String valor) throws StringIndexOutOfBoundsException, NumberFormatException {
		return crearFecha(valor);
	}

	private static String procesarDescripcion(String valor) {
		// Quito algunos valores wiki que no me interesan
		valor = limpiarPais(valor);
		// Transformo la notación wiki a HTML
		valor = WikiModel.toHtml(valor).replace("\n", "<br/>");
		return valor;
	}

	private static String procesarURL(String valor) {
		return valor.replace(" ", "_");
	}

	private static String procesarFechaActual() {
		return formatter.format(new Date());
	}

	private static String procesarCategoria() {
		return "deporte";
	}

	private static String procesarPonderacion() {
		return "1";
	}

	private static String procesarTitulo(String valor) {
		return valor;
	}

	private static String procesarLink(String valor) {
		valor = valor.replace(" ", "_");
		valor = Constantes.ENCABEZADO_URL + valor; 
		valor = WikiModel.toHtml(valor).replace("\n", "<br/>");
		return valor;
	}

	private static String procesarPais(String valor) throws ImposibleMapearEventoException {
		if (valor.indexOf("{{") != -1)
		{
			valor = limpiarPais(valor);
			String alfa3 = valor.substring(valor.indexOf("{{") + 2, valor.indexOf("{{") + 5);
			return PaisesUtil.iso3CountryCodeToIso2CountryCode(alfa3);
		}
		else
			throw new ImposibleMapearEventoException("Imposible mapear el valor '" + valor + "' a un país válido");
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

	private static String limpiarPais(String valor) {
		return valor.replace("selb|", "").replace("Selb|", "")
		.replace("sel|", "").replace("bandera|", "")
		.replace("bandera2|", "");
	}
}
