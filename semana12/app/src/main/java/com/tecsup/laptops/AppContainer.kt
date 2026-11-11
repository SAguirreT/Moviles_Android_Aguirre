package com.tecsup.laptops

import android.content.Context
import androidx.room.Room
import com.tecsup.laptops.data.auth.AuthRepository
import com.tecsup.laptops.data.auth.FirebaseAuthRepository
import com.tecsup.laptops.data.local.AppDatabase
import com.tecsup.laptops.data.local.ItemDao
import com.tecsup.laptops.notification.Notificador
import com.tecsup.laptops.notification.NotificadorAndroid

interface AppContainer {
    val itemDao: ItemDao
    val authRepository: AuthRepository
    val notificador: Notificador

    companion object {
        fun obtener(context: Context): AppContainer {
            return (context.applicationContext as LaptopsApplication).container
        }
    }
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "laptops_database"
        ).fallbackToDestructiveMigration(false).build()
    }

    override val itemDao: ItemDao by lazy {
        database.itemDao()
    }

    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    override val notificador: Notificador by lazy {
        NotificadorAndroid(context.applicationContext)
    }
}
