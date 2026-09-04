package com.aguirre.lab02

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS POO - TECH STORE ")
    println("=========================================")

    val nombreCliente = "Sheila Aguirre"
    println("Cliente: $nombreCliente")
    println()

    val tienda = TiendaCarrito()

    tienda.agregarProducto(Accesorio("Audifonos Bluetooth", 150.0, 2, true))
    tienda.agregarProducto(Accesorio("Cargador USB-C", 60.0, 3, false))
    tienda.agregarProducto(Software("Licencia Office 365", 250.0, 1, "Anual"))
    tienda.agregarProducto(Software("Antivirus Premium", 90.0, 1, "Mensual"))

    println()
    tienda.mostrarDetalle()
    println("Cantidad de productos: ${tienda.obtenerCantidadProductos()}")
    println()

    val subtotal = tienda.calcularSubtotal()
    val igv = tienda.calcularIGV(subtotal)
    val total = tienda.calcularTotal(subtotal, igv)

    println(String.format("Subtotal:        S/ %8.2f", subtotal))
    println(String.format("IGV (18%%):       S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR:   S/ %8.2f", total))
    println()

    val masCaro = tienda.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = tienda.calcularDescuento(total)
    val totalConDescuento = total - descuento
    if (descuento > 0) {
        println(String.format("Descuento aplicado: S/ %.2f", descuento))
    }
    println(String.format("TOTAL CON DESCUENTO: S/ %8.2f", totalConDescuento))
}