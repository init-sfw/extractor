package org.init.extractor.wikipedia.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.init.extractor.wikipedia.Pagina;

/** 
 * Clase que representa a un evento que será compatible al formato de Memoria, sobre el cual se harán los mapeos
 * de atributos extraídos de la ficha y demás datos necesarios
 * 
 * @author fbobbio
 *
 */
public class EventoMemoria {
	
	private List<AtributoEventoMemoria> atributos;
	private Pagina paginaPadre;
	
	public EventoMemoria()
	{
		atributos = new ArrayList<AtributoEventoMemoria>();
	}
	
	public EventoMemoria(Pagina padre)
	{
		atributos = new ArrayList<AtributoEventoMemoria>();
		this.paginaPadre = padre;
	}
	
	/**
	 * Constructor copia que sirve para tomar los valores cargados al modelo y poder seguir trabajando con una
	 * nueva instancia sin alterar la anterior
	 * 
	 * @param evt
	 */
	public EventoMemoria(EventoMemoria evt, Pagina padre)
	{
		//TODO: implementar deep copy o buscar la forma de resolver esto
		this.atributos = new ArrayList<AtributoEventoMemoria>(evt.atributos);
		this.paginaPadre = padre;
		Collections.copy(this.atributos, evt.atributos);
	}

	public List<AtributoEventoMemoria> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<AtributoEventoMemoria> atributos) {
		this.atributos = atributos;
	}

	public Pagina getPaginaPadre() {
		return paginaPadre;
	}

	public void setPaginaPadre(Pagina paginaPadre) {
		this.paginaPadre = paginaPadre;
	}
}
