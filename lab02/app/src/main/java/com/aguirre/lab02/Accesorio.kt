package com.aguirre.lab02

class Accesorio(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val tieneGarantia: Boolean
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        val recargoGarantia = if (tieneGarantia) precio * 0.05 else 0.0
        return (precio * cantidad) + recargoGarantia
    }
}