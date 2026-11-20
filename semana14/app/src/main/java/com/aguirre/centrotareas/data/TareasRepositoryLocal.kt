package com.aguirre.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {
    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    /**
     * Paso 5: Cuenta la cantidad de numeros primos desde 2 hasta [hasta] en Dispatchers.Default.
     * Incluye ensureActive() para verificar y responder a la cancelacion cooperativa durante la ejecucion del bucle.
     */
    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var contador = 0
        for (n in 2..hasta) {
            if (n % 2000 == 0) ensureActive()
            if (esPrimo(n)) contador++
        }
        contador
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var i = 2
        while (i * i <= n) {
            if (n % i == 0) return false
            i++
        }
        return true
    }
}
