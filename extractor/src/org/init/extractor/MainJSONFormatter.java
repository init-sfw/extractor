package org.init.extractor;

import org.init.extractor.utils.FileUtil;

/** 
 * Clase que se encarga de correr la utilidad particular de formatear un JSON
 * 
 * @author fbobbio
 *
 */
public class MainJSONFormatter {
	
	/**
	 * Método que ejecuta el programa de parseo de un JSON de un solo objeto a un JSON conteniendo un array de objetos
	 * 
	 * @param args Se deben enviar como parámetro de consola la url del archivo origen y la url del archivo destino deseado, en ambos casos url completas
	 */
	public static void main(String [] args)
	{
		try
		{
			FileUtil.convertJSONToProperFormat(args[0], args[1]);
		}
		catch (Exception e) {
			System.out.println("Se produjo un error y no se pudo parsear el archivo\n\nDetalles: " + e.getLocalizedMessage() + "\n\n");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
