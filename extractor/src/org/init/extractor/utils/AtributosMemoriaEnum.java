package org.init.extractor.utils;


/**
 * Clase que mantiene la enumeración de los atributos que memoria maneja internamente
 * 
 * @author fbobbio
 *
 */
public enum AtributosMemoriaEnum {
	FECHA("fecha"),
	FECHA_CREACION("fechaCreacion"),
	TITULO("titulo"),
	CATEGORIA("categoria"),
	PAIS("pais"),
	DESCRIPCION_BREVE("descripcionBreve"),
	LINK("link"),
	IMAGEN("imagen"),
	PONDERACION("ponderacion");
	
	private String nombre;
	
	private AtributosMemoriaEnum(String nombre)
	{
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
