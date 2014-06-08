package org.init.extractor.excepciones;

import org.init.extractor.wikipedia.eventos.AtributoEventoMemoria;
import org.init.extractor.wikipedia.eventos.EventoMemoria;

public class ImposibleMapearEventoException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6704746030452678917L;

	public ImposibleMapearEventoException()
	{
		super("No se pudo cargar el evento por algún error en el mapeo");
	}
	
	public ImposibleMapearEventoException(String msj)
	{
		super(msj);
	}
	
	public ImposibleMapearEventoException(String msj, Exception e)
	{
		super(msj,e);
	}
	
	public ImposibleMapearEventoException(EventoMemoria evt, Exception e)
	{
		super("No se pudo cargar el evento correspondiente a la página: " + evt.getPaginaPadre().getNombre(),e);
	}
	
	public ImposibleMapearEventoException(AtributoEventoMemoria att, Exception e)
	{
		super("No se pudo cargar el atributo " + att.getNombre(), e);
	}

}
