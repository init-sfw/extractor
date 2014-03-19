extractor
=========

Proyecto de extracción de datos de distintas fuentes con exportación a otras distintas.
Inicialmente creado con el objetivo de servir como fuente de datos al proyecto __Memoria__


## Versión actual

En la segunda versión de la app se hace una evolución a una arquitectura más seria pero aún inmadura, a modo de POC y con necesidad de mucha refactorización.

Sin tests, sin resoluciones arquitectónicas eficientes y sin performance.
Aún no escribe a disco el JSON de salida pero si lo aloja correctamente en las estructuras de datos de la arquitectura.

Se encuentra procesando unas 500 páginas de Wikipedia, con requests individuales (algo a mejorar), sobre torneos de fútbol (responden al template  [Ficha de torneo de fútbol](http://es.wikipedia.org/wiki/Plantilla:Ficha_de_torneo_de_f%C3%BAtbol) de las que extrae el infobox correspondiente y lo transforma en un evento procesable por Memoria.

## Ejecución

El proyecto está desarrollado en Eclipse Kepler y consta de una arquitectura simple y a mejorar la cual necesita de una conexión a internet para realizar los request.
La parametrización es manual desde el código, sin una interfaz práctica.
La salida queda pendiente para una próxima versión, aunque en versiones anteriores se estaba consiguiendo una salida decente.

## Próximo paso

La próxima versión tendrá como objetivo poder generar el archivo json de salida y quizás permitir trabajar con alguna otra plantilla de Wikipedia desde una parametrización interna.

## Más información

Más información sobre la investigación en marcha puede encontrarse en este documento (necesita permiso de acceso): https://docs.google.com/document/d/1oZ-4i0lSLBRiJDw0nS-wjFz4FVfhr-J-E1FOV8u3hv0
