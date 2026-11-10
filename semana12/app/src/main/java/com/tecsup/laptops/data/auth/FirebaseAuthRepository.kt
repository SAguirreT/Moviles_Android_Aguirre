package com.tecsup.laptops.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.tecsup.laptops.domain.model.Sesion
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override val estadoSesion: Flow<Sesion> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val usuario = firebaseAuth.currentUser
            if (usuario != null) {
                trySend(Sesion.Activa(usuario.uid, usuario.email ?: ""))
            } else {
                trySend(Sesion.Inactiva)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun iniciarSesion(email: String, clave: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, clave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cerrarSesion() {
        auth.signOut()
    }
}