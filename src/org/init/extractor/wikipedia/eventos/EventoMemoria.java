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
	
	/**
	 * Constructor copia que sirve para tomar los valores cargados al modelo y poder seguir trabajando con una
	 * nueva instancia sin alterar la anterior
	 * 
	 * @param padre la página padre del evento
	 */
	public EventoMemoria(Pagina padre)
	{
		this.paginaPadre = padre;
		this.atributos = new ArrayList<AtributoEventoMemoria>();
	}

	public void addAtributo(AtributoEventoMemoria atributo) {
		if (atributo!= null)
			this.atributos.add(atributo);
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
