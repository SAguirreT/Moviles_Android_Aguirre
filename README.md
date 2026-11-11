# Laboratorio Integrador – Catálogo de Laptops

**Integrantes:** Sheila Aguirre  
**Curso:** Programación en Móviles – Docente: Juan León S.

## 1. Tema y variante
- **Tema:** Productos (DummyJSON)
- **Variante:** categoría `laptops`
- **Paquete:** `com.tecsup.laptops`
- **Endpoints:**
  - Lista: `https://dummyjson.com/products/category/laptops`
  - Detalle: `https://dummyjson.com/products/{id}`

## 2. Tecnologías
Jetpack Compose + Material 3 · MVVM · Retrofit · Coil · Room · Firebase Auth + Firestore · Notificaciones locales

## 3. Cómo compilar
1. Clonar el repositorio y abrirlo en Android Studio.
2. Crear un proyecto en Firebase, registrar la app con el paquete `com.tecsup.laptops`.
3. Descargar `google-services.json` y colocarlo en `app/`.
4. Sync Gradle → Clean Project → Rebuild.
5. Ejecutar en un emulador con Google Play.

## 4. Cómo crear los usuarios de prueba
1. Firebase → Authentication → Método de acceso: activar Correo/contraseña.
2. Authentication → Usuarios: crear `admin@catalogo.com` y `usuario@catalogo.com`.
3. Firestore: colección `usuarios`, un documento por usuario cuyo ID es el **UID** del usuario, con los campos:
   - `rol` (texto): `admin` o `usuario`
   - `nombre` (texto)
4. Publicar las reglas de Firestore.

| Usuario | Rol | Correo de prueba |
|---|---|---|
| Admin | admin | admin@catalogo.com |
| Usuario | usuario | usuario@catalogo.com |

## 5. Arquitectura
El proyecto sigue una arquitectura limpia basada en MVVM (Model-View-ViewModel) y principios SOLID:
- **Capa Data**: Gestiona las peticiones remotas con Retrofit (`ApiService`), la persistencia local con Room (`ItemDao`, `ItemEntity`), y la autenticación con Firebase.
- **Capa Domain**: Contiene los modelos de negocio puros (`Item`, `Sesion`) independientes de frameworks externos. El `ItemRepository` es una interfaz que abstrae el origen de los datos.
- **Capa UI**: Construida completamente con Jetpack Compose y Material 3, utilizando StateFlow para la reactividad de los ViewModels.
- **Single Source of Truth**: Room y los repositorios aseguran que la UI reaccione a fuentes de datos sincronizadas.

## 6. Capturas
| Login | Lista | Detalle |
|---|---|---|
![img.png](img.png)![img_3.png](img_3.png)

| Favoritos | Panel Admin | Notificación |
|---|---|---|
![img_1.png](img_1.png)![img_2.png](img_2.png)![img_4.png](img_4.png)


## 7. Reparto de tareas
| Integrante | Hitos / tareas realizadas |
|---|---|
| Sheila Aguirre | Implementación completa de la UI con Jetpack Compose y Material 3, consumo de la API DummyJSON con Retrofit y Coil, persistencia con Room, gestión de Favoritos en tiempo real, integración con Firebase Auth y sistema de notificaciones locales con canales y permisos. |
