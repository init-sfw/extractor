package org.init.extractor;

import java.net.MalformedURLException;

import org.init.extractor.wikipedia.Plantilla;

/**
 * Clase principal encargada de dar inicio a las tareas del extractor.
 * 
 * @author fbobbio
 * @version 17/03/2014
 *
 */
public class Main {
	
	public static void main (String [] args) throws MalformedURLException
	{
		Plantilla plantilla = new Plantilla("Ficha_de_torneo_de_fútbol");
		Extractor extractor = new Extractor(plantilla);
		extractor.init();
	}

}
