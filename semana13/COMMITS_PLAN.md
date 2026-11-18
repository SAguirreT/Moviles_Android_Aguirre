# Plan de commits según GLAB-S13

Los 10 commits de `main` corresponden a la Fase 1, y la rama `mejora-ia` se crea al terminar esa fase.

| N.º | Título | Archivos / descripción |
|---|---|---|
| 1 | chore: importa proyecto base CampusGPS | Configuración Gradle, MainActivity, model, data, viewmodel, ui y theme: proyecto inicial. |
| 2 | chore: configura permisos y clave de Maps con local.properties | AndroidManifest.xml, app/build.gradle.kts y local.properties (excluido de Git): permisos y clave Maps. |
| 3 | feat(ubicacion): pide permiso de ubicacion en tiempo de ejecucion | ui/PantallaMapa.kt: solicitud de permisos y estado de denegación. |
| 4 | feat(mapa): muestra mapa con marcador de la ubicacion actual | ui/MapaUbicacion.kt, ui/PantallaMapa.kt: mapa, cámara y marcador propio. |
| 5 | feat(mapa): agrega marcadores de los lugares del campus | ui/MapaUbicacion.kt: cinco marcadores azules. |
| 6 | feat(distancia): calcula y ordena lugares por distancia | util/Distancia.kt, util/Formato.kt, viewmodel/MapaViewModel.kt: distancias y orden. |
| 7 | feat(ui): agrega lista de lugares y seleccion en el mapa | ui/ListaLugares.kt, ui/MapaUbicacion.kt, ui/PantallaMapa.kt, viewmodel/MapaViewModel.kt: tarjetas y selección. |
| 8 | feat(ui): resalta y navega al lugar mas cercano | viewmodel/MapaViewModel.kt, ui/MapaUbicacion.kt, ui/PantallaMapa.kt: marcador verde y navegación. |
| 9 | feat(errores): maneja permiso denegado, GPS apagado y sin Play Services | model/Modelos.kt, data/UbicacionRepository.kt, data/UbicacionRepositoryGoogle.kt, viewmodel/MapaViewModel.kt, ui/MensajeError.kt: errores tipados y acciones. |
| 10 | docs: agrega README con estructura y capturas | README.md y capturas: documentación y pruebas de aceptación. |

## Rama mejora-ia
La guía requiere **mínimo tres mejoras** y **mínimo tres commits**, incluyendo PROMPTS.md. Los títulos y archivos específicos se definirán al seleccionar las mejoras.

**Nota:** El PDF establece que la Fase 1 se realiza sin IA. El proyecto de este ZIP es una reconstrucción asistida, por lo que no debe presentarse como trabajo realizado sin IA.
