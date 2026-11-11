package com.tecsup.laptops

import android.app.Application
import com.google.firebase.FirebaseApp

class LaptopsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = DefaultAppContainer(this)
    }
}
