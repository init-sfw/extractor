package org.init.extractor.wikipedia.eventos;

/**
 * Clase que representa a un atributo en particular de la ficha del template de la página de Wikipedia
 * 
 * @author fbobbio
 *
 */
public class AtributoExtranjero {
	
	private String nombre;
	private String valor;
	private AtributoEventoMemoria eventoMapeado;
	
	public AtributoExtranjero()
	{
		this.nombre = null;
		this.valor = null;
	}
	
	public AtributoExtranjero(String nombre)
	{
		this.nombre = nombre;
		this.valor = null;
	}
	
	public AtributoExtranjero(String nombre, String valor)
	{
		this.nombre = nombre;
		this.valor = valor;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public AtributoEventoMemoria getEventoMapeado() {
		return eventoMapeado;
	}
	public void setEventoMapeado(AtributoEventoMemoria eventoMapeado) {
		this.eventoMapeado = eventoMapeado;
	}
	
	@Override
	public String toString() {
		return valor;
	}
}
