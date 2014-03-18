package org.init.memoria.futbol;

import static org.junit.Assert.*;

import org.init.memoria.futbol.eventos.MapaAtributosMapeados;
import org.junit.Test;

public class MapaAtributosMapeadoTest {

	@Test
	public void testConversionPlantilla() {
		MapaAtributosMapeados mapa = new MapaAtributosMapeados();
		mapa.setPlantillaMapeo("Hola esto es una {prueba} de lo que {multiple} hacerse con un MapaAtributosMapeados");
		
	}

}
