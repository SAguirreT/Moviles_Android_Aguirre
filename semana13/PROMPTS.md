Registro de prompts y mejoras asistidas por IA — CampusGPS
Estudiante: Sheila Aguirre
Proyecto: CampusGPS — Semana 13
Tecnologías: Kotlin, Jetpack Compose, Material 3, Google Maps Compose, StateFlow y arquitectura MVVM
Repositorio: https://github.com/SAguirreT/Moviles_Android_Aguirre
Rama de mejoras: mejora-ia-semana13
1. Introducción
   Este documento registra el uso de inteligencia artificial como apoyo para implementar tres mejoras incrementales en CampusGPS. La aplicación permite consultar la ubicación actual, visualizar lugares de referencia en Google Maps, calcular distancias, seleccionar ubicaciones y mostrar mensajes de error. Las mejoras se desarrollaron con el propósito de modernizar la interfaz y facilitar la exploración del campus sin reemplazar la arquitectura MVVM existente.

Se documentan los prompts de trabajo reconstruidos a partir de las solicitudes realizadas durante el desarrollo, las propuestas de implementación, los archivos involucrados, las decisiones de integración y las pruebas necesarias para validar cada cambio. Los textos de los prompts son una síntesis fiel de los objetivos solicitados, no una transcripción literal de todos los mensajes de la conversación.
2. Resumen de los tres commits
   Commit	Mensaje de commit propuesto	Mejora principal	Archivos principales
   1	feat(ui): implementa tema oscuro profesional y mapa nocturno	Interfaz oscura con Material 3 y estilo nocturno para Google Maps	Color.kt, Theme.kt, MainActivity.kt, MapaUbicacion.kt, ListaLugares.kt, MensajeError.kt, PantallaMapa.kt, map_style_dark.json
   2	feat(filtros): agrega filtros por categoria en mapa y lista	Filtrado reactivo de lugares por categoría	MapaViewModel.kt, FiltrosCategoria.kt, PantallaMapa.kt
   3	feat(mapa): implementa radio de cercania interactivo con Google Maps	Selección de radio, círculo geográfico y estado dentro/fuera	MapaViewModel.kt, SelectorRadio.kt, MapaUbicacion.kt, ListaLugares.kt, PantallaMapa.kt


Nota: Los mensajes anteriores describen la intención de los commits. Para registrar sus identificadores exactos y confirmar qué archivos quedaron incluidos en Git, consultar git log --oneline y git show --stat <hash>.

3. Commit 1 — Diseño profesional en modo oscuro
   3.1 Objetivo
   Rediseñar CampusGPS con una apariencia moderna, consistente y legible, utilizando una paleta oscura de tonos grafito y violeta. Aplicar también un estilo nocturno al mapa para evitar el contraste entre la interfaz oscura y el mapa claro, conservando los marcadores, la ubicación actual, la selección de lugares, las distancias y los mensajes de error.
   3.2 Prompt de trabajo utilizado (síntesis)
   Implementar un modo oscuro profesional para CampusGPS con Jetpack Compose y Material 3. Utilizar colores grafito y violeta, tarjetas redondeadas, botones modernos y un estilo nocturno para Google Maps. Mantener la arquitectura MVVM y todas las funcionalidades existentes. Entregar los archivos completos y comentados en español, incluyendo los cambios necesarios para que MainActivity.kt utilice correctamente el tema.

3.3 Propuesta y desarrollo asistido por IA
La IA propuso centralizar los colores en Color.kt, definir un esquema darkColorScheme en Theme.kt, envolver la interfaz con el tema personalizado desde MainActivity.kt y aplicar a Google Maps un archivo JSON de estilos cargado mediante MapStyleOptions.loadRawResourceStyle. También propuso adaptar las tarjetas de lugares, los botones y los mensajes de error a la misma identidad visual.

Durante la integración se detectó el error Unresolved reference 'CampusGPSTema', ocasionado por una diferencia entre el nombre utilizado por MainActivity.kt y el nombre de la función definida en Theme.kt. Se corrigió definiendo CampusGPSTema y manteniendo un alias CampusGPSTheme para compatibilidad.
3.4 Archivos involucrados
Archivo	Modificación realizada
ui/theme/Color.kt	Definición de colores para fondos, superficies, texto, bordes, selección y errores.
ui/theme/Theme.kt	Configuración del tema oscuro Material 3 y corrección del nombre CampusGPSTema.
MainActivity.kt	Aplicación del tema personalizado al contenido Compose.
ui/MapaUbicacion.kt	Carga del estilo nocturno del mapa, manteniendo marcadores y cámara.
ui/ListaLugares.kt	Tarjetas oscuras, selección resaltada y visualización de distancias.
ui/MensajeError.kt	Presentación de errores con colores legibles en modo oscuro.
ui/PantallaMapa.kt	Rediseño del encabezado, botones y distribución de la pantalla.
res/raw/map_style_dark.json	Reglas de estilo nocturno para calles, edificios, etiquetas, parques y agua.


3.5 Decisiones y justificación
- Aceptar un tema centralizado en Material 3 para mantener consistencia visual.
- Aceptar un JSON local para el mapa nocturno, evitando nuevas dependencias.
- Corregir el nombre del tema para resolver la referencia no encontrada.
- Conservar la arquitectura MVVM y los repositorios originales, porque esta mejora es principalmente visual.
  3.6 Pruebas de verificación
- [ ] Compilar el proyecto sin errores.
- [ ] Confirmar que el encabezado, los botones y las tarjetas se visualizan en modo oscuro.
- [ ] Confirmar que Google Maps utiliza el estilo nocturno.
- [ ] Obtener la ubicación y verificar el marcador rojo.
- [ ] Seleccionar un lugar y comprobar el desplazamiento de la cámara.
- [ ] Verificar que las distancias y los mensajes de error siguen siendo legibles.
  Evidencias sugeridas: captura de la pantalla principal oscura, mapa nocturno y selección de una tarjeta.
4. Commit 2 — Filtros interactivos por categoría
   4.1 Objetivo
   Facilitar la búsqueda de lugares del campus mediante filtros visuales que permitan mostrar únicamente las ubicaciones de una categoría. Sincronizar el resultado entre la lista inferior y los marcadores de Google Maps, conservando el diseño oscuro y la selección de lugares.
   4.2 Prompt de trabajo utilizado (síntesis)
   Agregar filtros interactivos por categoría a CampusGPS, manteniendo el diseño oscuro profesional. Crear chips horizontales para las opciones Todos, Estudio, Comida, Eventos y Servicios. Implementar la lógica en el ViewModel con StateFlow, actualizar simultáneamente la lista y el mapa, y conservar la ubicación actual, el cálculo de distancias y la arquitectura MVVM. Proporcionar el código completo y comentado en español.

4.3 Propuesta y desarrollo asistido por IA
Se propuso crear FiltrosCategoria.kt como componente visual reutilizable con FilterChip de Material 3. En MapaViewModel.kt se incorporó el estado categoriaSeleccionada y un flujo lugaresFiltrados construido mediante combine, para derivar la lista visible sin modificar los datos originales. PantallaMapa.kt pasó a utilizar esa lista tanto en el mapa como en las tarjetas.

También se contempló limpiar la selección cuando el lugar elegido deja de pertenecer a la categoría activa y recalcular el lugar más cercano entre los elementos filtrados. La barra de filtros se diseñó con desplazamiento horizontal para adaptarse a pantallas pequeñas.
4.4 Archivos involucrados
Archivo	Modificación realizada
viewmodel/MapaViewModel.kt	Estado de categoría, lista filtrada reactiva y actualización de selección y lugar cercano.
ui/FiltrosCategoria.kt	Nuevo componente de chips horizontales con estilo oscuro.
ui/PantallaMapa.kt	Integración de los filtros y entrega de la lista filtrada al mapa y las tarjetas.


MapaUbicacion.kt y ListaLugares.kt reutilizan sus parámetros de lista existentes, por lo que no fue necesario modificar esos componentes para este commit.
4.5 Decisiones y justificación
- Aceptar el uso de StateFlow y combine para mantener una única fuente de verdad en el ViewModel.
- Aceptar los chips horizontales para que la navegación por categorías sea visible y sencilla.
- Conservar la lista original sin alteraciones, permitiendo regresar a la opción Todos.
- Verificar que las categorías utilizadas en los chips coincidan con las definidas en LugaresRepositoryLocal.kt; de lo contrario, una categoría podría mostrarse vacía.
  4.6 Pruebas de verificación
- [ ] Comprobar que Todos muestra todos los lugares.
- [ ] Verificar que cada categoría muestra únicamente los lugares correspondientes.
- [ ] Confirmar que el mapa y la lista presentan el mismo conjunto filtrado.
- [ ] Seleccionar una tarjeta y comprobar el desplazamiento del mapa.
- [ ] Cambiar de categoría y verificar que una selección no visible se desmarca.
- [ ] Confirmar que el marcador de ubicación actual sigue disponible.
  Evidencias sugeridas: capturas con Todos y con al menos dos categorías diferentes.
5. Commit 3 — Radio de cercanía interactivo
   5.1 Objetivo
   Permitir que el usuario seleccione un radio de 100, 200 o 500 metros alrededor de su ubicación actual. Representar visualmente el área mediante un círculo violeta sobre Google Maps, contar los lugares comprendidos en el radio y mostrar en cada tarjeta si el lugar está dentro o fuera de la distancia seleccionada.
   5.2 Prompt de trabajo utilizado (síntesis)
   Implementar una tercera mejora visual para CampusGPS: un selector de radio de 100, 200 y 500 metros, con un círculo violeta semitransparente alrededor de la ubicación actual en Google Maps. Actualizar el contador de lugares dentro del radio y agregar etiquetas de cercanía en las tarjetas. Mantener el modo oscuro, los filtros por categoría, el cálculo de distancias y la arquitectura MVVM. Entregar los archivos completos y comentados en español.

5.3 Propuesta y desarrollo asistido por IA
Se propuso agregar radioMetros como StateFlow en MapaViewModel.kt, con un valor inicial de 200 metros y una función que acepta únicamente 100, 200 o 500. Para calcular el contador se combinó el radio seleccionado con lugaresFiltrados, de modo que el número se actualice cuando cambien la ubicación, el filtro o el radio.

Se creó SelectorRadio.kt con tres controles visuales y se integró en PantallaMapa.kt. En MapaUbicacion.kt se añadió un Circle de Google Maps centrado en la posición actual, con borde violeta y relleno semitransparente. En ListaLugares.kt se incorporaron las etiquetas Dentro del radio, Fuera del radio y Ubicación pendiente.

Los lugares fuera del radio continúan visibles en la lista y en el mapa; el radio señala proximidad, pero no oculta lugares. Los filtros por categoría siguen determinando qué lugares aparecen.
5.4 Archivos involucrados
Archivo	Modificación realizada
viewmodel/MapaViewModel.kt	Estado radioMetros, función cambiarRadio y contador reactivo cantidadDentroRadio.
ui/SelectorRadio.kt	Nuevo selector de 100, 200 y 500 metros.
ui/MapaUbicacion.kt	Círculo geográfico violeta alrededor de la ubicación actual y descripción de cercanía en marcadores.
ui/ListaLugares.kt	Indicadores de lugar dentro o fuera del radio, o ubicación pendiente.
ui/PantallaMapa.kt	Integración del selector, el contador y los nuevos parámetros del mapa y la lista.


5.5 Decisiones y justificación
- Aceptar tres valores de radio predeterminados para facilitar su uso en dispositivos móviles.
- Aceptar Circle de Google Maps Compose, que representa un radio geográfico en metros sin incorporar dependencias adicionales.
- Aceptar el contador reactivo en el ViewModel, evitando duplicar cálculos en la interfaz.
- Conservar los lugares fuera del radio, identificándolos mediante etiquetas para no perder información útil.
- Aclarar que el radio y las distancias corresponden a proximidad geográfica aproximada en línea recta, no a recorridos peatonales por calles.
  5.6 Pruebas de verificación
- [ ] Compilar y ejecutar el proyecto sin errores.
- [ ] Pulsar Mi ubicación y confirmar que aparece el círculo violeta.
- [ ] Seleccionar 100, 200 y 500 metros y verificar que cambia el tamaño del círculo.
- [ ] Confirmar que el contador cambia de acuerdo con las distancias disponibles.
- [ ] Verificar las etiquetas de dentro y fuera del radio en las tarjetas.
- [ ] Comprobar que los filtros por categoría siguen funcionando.
- [ ] Confirmar que el botón Más cercano y la selección de marcadores se mantienen operativos.
  Evidencias sugeridas: capturas del mapa con radio de 100 y 500 metros y de las tarjetas con indicadores de cercanía.
6. Metodología de integración y control de versiones
   Las mejoras se organizaron en commits independientes dentro de la rama mejora-ia-semana13. Los cambios se editaron y probaron en Android Studio y posteriormente se copiaron al repositorio local, conservando la estructura semana13.

El flujo de trabajo previsto para cada mejora fue:
1. Definir el objetivo funcional y visual.
2. Solicitar una propuesta de implementación asistida por IA.
3. Revisar los archivos y adaptar el código al proyecto.
4. Corregir errores de integración y compatibilidad.
5. Compilar y probar la funcionalidad en el emulador.
6. Copiar los archivos finales al repositorio.
7. Revisar git diff, crear un commit descriptivo y subirlo a GitHub.
   La clave de Google Maps permanece en local.properties, que debe estar excluido de Git mediante .gitignore. No debe incorporarse a los commits ni publicarse en el repositorio.
7. Reflexión sobre el uso de IA
   La inteligencia artificial permitió proponer componentes visuales, organizar el estado de la aplicación y acelerar la redacción de código y documentación. Sin embargo, sus propuestas requirieron revisión: por ejemplo, fue necesario resolver la discrepancia en el nombre del tema y comprobar la compatibilidad de las nuevas funciones con los modelos y repositorios existentes.

La IA se utilizó como herramienta de apoyo y no como sustituto de la validación técnica. La compilación, las pruebas en el emulador y la revisión de Git son necesarias para confirmar que cada mejora funciona correctamente.
8. Conclusiones
1. Implementé una propuesta de interfaz oscura con Material 3 para mejorar la presentación y consistencia visual de CampusGPS.
2. Incorporé filtros por categoría mediante componentes Compose y flujos reactivos, manteniendo separada la lógica del ViewModel.
3. Desarrollé un selector de radio de cercanía que permite visualizar distancias geográficas y distinguir lugares próximos.
4. Conservé las funcionalidades principales de ubicación, marcadores, distancias y selección durante el diseño de las mejoras.
5. Documenté los prompts, las decisiones técnicas y las verificaciones necesarias para dar trazabilidad al trabajo asistido por IA.
9. Estado de validación final
   Estado documental: tres mejoras descritas.
   Compilación final: por confirmar con la ejecución del proyecto.
   Pruebas funcionales: marcar las casillas anteriores después de comprobar cada caso.
   Commits y hashes reales: verificar con git log --oneline antes de la entrega.

Autora: Sheila Aguirre