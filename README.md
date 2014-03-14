extractor
=========

Proyecto de extracción de datos de distintas fuentes con exportación a otras distintas.
Inicialmente creado con el objetivo de servir como fuente de datos al proyecto __Memoria__


## Versión actual

Se cuenta con una versión inicial, POC, en JAVA con mucho hardcodeo y sin manejo de problemas o cambios.

Se encuentra procesando unas 500 páginas de Wikipedia, con requests individuales (algo a mejorar), sobre torneos de fútbol (responden al template  [Ficha de torneo de fútbol](http://es.wikipedia.org/wiki/Plantilla:Ficha_de_torneo_de_f%C3%BAtbol) de las que extrae el infobox correspondiente y lo transforma en un evento procesable por Memoria.

## Ejecución

El proyecto está desarrollado en Eclipse Kepler y consta de una sola clase ejecutable _Futbol.java_ la cual necesita de una conexión a internet para realizar los request.
La parametrización es manual desde el código, sin una interfaz práctica.
La salida es por consola y a la escritura de un archivo llamado _datos-mundial.json_ que puede ser procesado por Memoria.

## Próximo paso

La próxima versión debería pensarse en trabajar con SPARQL a través de la herramienta [dbpedia](http://dbpedia.org/About) para permitir una mayor flexibilidad y mejor live performance, además de poder realizar más completos y automatizables sets de datos.

## Más información

Más información sobre la investigación en marcha puede encontrarse en este documento (necesita permiso de acceso): https://docs.google.com/document/d/1oZ-4i0lSLBRiJDw0nS-wjFz4FVfhr-J-E1FOV8u3hv0
