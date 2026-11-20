# Laboratorio 14 — CentroTareas (Concurrencia y Corrutinas en Android)

## 1. Objetivo
Desarrollar y comprender el manejo de la concurrencia asíncrona, corrutinas de Kotlin, flujos de estado (`StateFlow`), hilos (`Dispatchers`), tareas concurrentes (`async`), cancelación de operaciones con `Job` y gestión de errores en una aplicación Android desarrollada con Jetpack Compose y arquitectura MVVM.

## 2. Tecnologías y Librerías
- **Lenguaje:** Kotlin 2.0.21
- **UI:** Jetpack Compose & Material 3 (BOM 2024.10.01)
- **Concurrencia:** Kotlin Coroutines (`viewModelScope`, `Dispatchers.Default`, `withContext`, `async`, `await`, `coroutineScope`, `delay`, `ensureActive`)
- **Gestión de Estado:** StateFlow / MutableStateFlow
- **Arquitectura:** MVVM (Model - View - ViewModel) + Repository Pattern

## 3. Arquitectura MVVM y Estructura de Carpetas
```
com.aguirre.centrotareas/
│
├── data/
│   ├── TareasRepository.kt           # Interface del repositorio de tareas
│   └── TareasRepositoryLocal.kt      # Implementación local con operaciones asíncronas y simulación de red/CPU
│
├── model/
│   └── Estados.kt                    # Sealed interfaces para estados UI (PrimosUi, CargaUi)
│
├── ui/
│   ├── PantallaTareas.kt             # Interfaz de usuario declarativa en Compose con Material 3
│   └── theme/
│       └── Theme.kt                  # Tema Material 3 de la aplicación
│
├── viewmodel/
│   ├── TareasViewModel.kt            # ViewModel que expone StateFlows y lógica de negocio asíncrona
│   └── TareasViewModelFactory.kt     # Factory para inyección de dependencias del repositorio
│
└── MainActivity.kt                   # Activity principal de punto de entrada
```

## 4. Observaciones Técnicas: Congelamiento vs Corrutinas
- **Bloqueo del Hilo Principal (`Thread.sleep`):** Al ejecutar operaciones pesadas de 5 segundos utilizando `Thread.sleep` en el hilo principal (`Main Thread`), la aplicación se congela por completo, impidiendo redibujar la interfaz o responder a interacciones táctiles (como el contador "Tócame"). Esto activa el diálogo ANR (*Application Not Responding*).
- **Solución con Corrutinas (`withContext` y `delay`):** Al trasladar el trabajo pesado a una corrutina utilizando `withContext(Dispatchers.Default)` y empleando funciones suspendidas no bloqueantes como `delay`, el hilo principal queda libre para procesar eventos de UI y animaciones fluidamente.

## 5. Resultados de Pruebas
1. **Tarea con progreso:** Muestra actualización reactiva en tiempo real del porcentaje (0% al 100%), barra de progreso lineal (`LinearProgressIndicator`) y capacidad de cancelación instantánea mediante `Job.cancel()`.
2. **Contar números primos:** Algoritmo optimizado ejecutado en `Dispatchers.Default` con cancelación cooperativa (`ensureActive()`), reportando cantidad exacta de primos y duración en milisegundos.
3. **Dos cargas (Secuencial vs Paralelo):**
   - *Carga secuencial (`cargarUnaPorUna`):* Ejecuta perfil (2s) y pedidos (3s) uno tras otro (~5000 ms).
   - *Carga en paralelo (`cargarALaVez`):* Ejecuta ambas peticiones simultáneamente con `async` / `await` dentro de un `coroutineScope`, reduciendo el tiempo total al de la tarea más lenta (~3000 ms).
4. **Manejo de errores y reintentos:** Simulación de errores de red (`IOException`) con captura de excepciones en ViewModel y opción de reintento en la UI.

## 6. Preguntas de Reflexión
1. **¿Qué diferencia existe entre bloquear el hilo principal y suspender una corrutina?**
   Bloquear el hilo principal detiene la ejecución del hilo de UI, impidiendo que el sistema renderice fotogramas o procese entradas táctiles (provocando ANR). Suspender una corrutina libera el hilo para que realice otras tareas mientras espera que finalice una operación asíncrona, reanudándose posteriormente sin bloquear el hilo.
2. **¿Cuándo se debe utilizar `Dispatchers.Default` frente a `Dispatchers.IO`?**
   `Dispatchers.Default` está optimizado para operaciones intensivas de CPU (como cálculos matemáticos, procesamiento de imágenes o conteo de números primos). `Dispatchers.IO` está optimizado para operaciones de entrada/salida (I/O), como llamadas a redes, lectura/escritura de bases de datos o archivos.
3. **¿Cómo funciona la cancelación cooperativa en Kotlin Coroutines?**
   Las corrutinas no pueden ser canceladas abruptamente desde el exterior por motivos de seguridad de recursos. La cancelación es cooperativa: se solicita la cancelación de un `Job`, y la corrutina la detecta mediante puntos de suspensión cooperativos (como `delay`) o llamadas explícitas a `ensureActive()`, lanzando una `CancellationException`.
4. **¿Por qué `async` y `await` permiten paralelizar tareas de red o base de datos?**
   `async` inicia una nueva corrutina hijo y devuelve un `Deferred<T>` que representa un futuro resultado. Al iniciar múltiples `async` antes de llamar a `await`, ambas tareas se ejecutan concurrentemente. `await` suspende la ejecución hasta que cada resultado esté disponible, permitiendo solapar tiempos de espera (I/O).
5. **¿Qué ventajas aporta el uso de `StateFlow` en combinación con Jetpack Compose frente a LiveData?**
   `StateFlow` forma parte nativa de Kotlin Coroutines, maneja siempre un valor inicial, es agnóstico al ciclo de vida de la UI (requiriendo `collectAsStateWithLifecycle()` o `collectAsState()`), soporta operadores de Flow avanzados y no requiere dependencias adicionales de Android lifecycle en capas puras de repositorio o ViewModel.
