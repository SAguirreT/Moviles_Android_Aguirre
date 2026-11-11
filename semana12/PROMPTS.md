# Registro de consultas a IA

> Completar en la Fase 2 (rama `mejora-ia`), una entrada por consulta.

## Consulta 1
- Fecha: 2026-11-11
- Herramienta: Android Studio AI Assistant
- Prompt: Creación de pruebas unitarias para `ListaViewModel` usando fakes escritos a mano (`FakeItemRepository` y `FakeNotificador`), configuración de corrutinas con `kotlinx-coroutines-test`, y validación de escenarios de éxito y fallo de red con/sin datos guardados.
- Qué respondió (resumen): Proveedor de los fakes manuales, configuración de test dispatchers (`UnconfinedTestDispatcher`), y las 4 pruebas unitarias descriptivas en español bajo el patrón Given/When/Then.
- Qué usamos / qué cambiamos y por qué: 
  - Se agregaron las dependencias `junit` y `kotlinx-coroutines-test` en `app/build.gradle.kts`.
  - Se estructuró `ListaViewModel` para interactuar con `ItemRepository` (`repo.items`, `refrescar()`, `cantidadGuardada()`).
  - Se creó el archivo `app/src/test/java/com/tecsup/laptops/ui/lista/ListaViewModelTest.kt`.
- Qué aprendimos: Cómo configurar el hilo principal (`Dispatchers.setMain`) en entornos de prueba unitarios para ViewModel y cómo los fakes escritos a mano ofrecen pruebas más limpias, rápidas y legibles sin necesidad de librerías de mocking complejas.
