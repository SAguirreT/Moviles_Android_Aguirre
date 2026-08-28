package com.aguirre.lab02

class Software(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val tipoLicencia: String
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        return precio * cantidad
    }
}