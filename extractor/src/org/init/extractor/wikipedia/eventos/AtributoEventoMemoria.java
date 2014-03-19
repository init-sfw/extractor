package org.init.extractor.wikipedia.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;

import org.init.extractor.excepciones.ImposibleMapearEventoException;
import org.init.extractor.utils.ArrangeEnum;
import org.init.extractor.utils.ArrangeProcessor;

/**
 * Clase que representa a un atributo de un evento que será compatible al
 * formato de Memoria, sobre el cual se harán los mapeos de atributos extraídos
 * de la ficha y demás datos necesarios
 * 
 * @author fbobbio
 * 
 */
public class AtributoEventoMemoria {

	private String nombre;
	private Object tipoDato;
	private MapaAtributosMapeados listaMapeos;
	private String valorFinal;
	private List<ArrangeEnum> arranges;

	public AtributoEventoMemoria(String nombre, Object tipoDato) {
		this.nombre = nombre;
		this.tipoDato = tipoDato;
		arranges = new ArrayList<ArrangeEnum>();
	}

	/**
	 * Método que procesa el atributo cargado según los arrenges necesarios
	 */
	public void procesar() throws ImposibleMapearEventoException {
		valorFinal = listaMapeos.toString();
		if (arranges != null) {
			for (ArrangeEnum e : arranges) {
				try
				{
					valorFinal = ArrangeProcessor.procesarArrange(e, valorFinal);
				}
				catch (StringIndexOutOfBoundsException ex)
				{
					throw new ImposibleMapearEventoException(this, ex);
				}
				catch (NumberFormatException ex)
				{
					throw new ImposibleMapearEventoException(this, ex);
				}
			}
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Object getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(Object tipoDato) {
		this.tipoDato = tipoDato;
	}

	public MapaAtributosMapeados getListaMapeos() {
		return listaMapeos;
	}

	public void setListaMapeos(MapaAtributosMapeados listaMapeos) {
		this.listaMapeos = listaMapeos;
	}

	public List<ArrangeEnum> getArranges() {
		return arranges;
	}

	public void setArranges(List<ArrangeEnum> arranges) {
		this.arranges = arranges;
	}

	public String getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(String valorFinal) {
		this.valorFinal = valorFinal;
	}
}
