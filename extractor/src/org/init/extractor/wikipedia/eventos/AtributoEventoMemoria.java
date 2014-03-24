package org.init.extractor.wikipedia.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;

import org.init.extractor.Constantes;
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
	private MapaAtributosMapeados listaMapeos;
	private String valorFinal;
	private List<ArrangeEnum> arranges;
	private EventoMemoria eventoPadre;

	public AtributoEventoMemoria(String nombre, EventoMemoria evt) {
		this.nombre = nombre;
		this.eventoPadre = evt;
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
					switch (e) //TODO: Chanchada
					{
					case TITULO_PAGINA: 
						valorFinal = this.eventoPadre.getPaginaPadre().getNombre(); 
						break;
					case LINK:
						valorFinal = Constantes.ENCABEZADO_URL + this.eventoPadre.getPaginaPadre().getNombre().replace(" ", "_"); 
						break;
					default:
						valorFinal = ArrangeProcessor.procesarArrange(e, valorFinal);
						break;
					}
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

	public EventoMemoria getEventoPadre() {
		return eventoPadre;
	}

	public void setEventoPadre(EventoMemoria eventoPadre) {
		this.eventoPadre = eventoPadre;
	}
}
