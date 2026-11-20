
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// COMMIT 2: Cargar la clave de Google Maps de forma segura.
val localProps = Properties().apply {
    val archivo = rootProject.file("local.properties")

    if (archivo.exists()) {
        archivo.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.aguirre.campusgps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aguirre.campusgps"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // COMMIT 2: Enviar la clave al AndroidManifest.
        manifestPlaceholders["MAPS_API_KEY"] =
            localProps.getProperty("MAPS_API_KEY", "")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Jetpack Compose y Material 3
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Arquitectura MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Servicios de ubicacion de Google
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Corrutinas para Google Play Services
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    // Google Maps con Jetpack Compose
    implementation("com.google.maps.android:maps-compose:8.4.0")

    // Herramientas de depuracion
    debugImplementation("androidx.compose.ui:ui-tooling")
}
