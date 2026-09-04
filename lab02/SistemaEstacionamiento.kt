
fun main() {

    println("==========================================")
    println("       SISTEMA DE ESTACIONAMIENTO")
    println("==========================================")

    // INPUTS
    print("Ingrese la placa: ")
    val placa = readln()

    print("Ingrese el tipo de vehículo (Moto/Auto/Camioneta/Trailer): ")
    val tipoVehiculo = readln()

    print("Ingrese la cantidad de horas: ")
    val horas = readln().toInt()

    print("¿Es cliente frecuente? (S/N): ")
    val clienteFrecuente = readln()

    // TARIFA BASE
    var tarifaHora = 0.0

    if (tipoVehiculo.equals("Moto", ignoreCase = true)) {
        tarifaHora = 2.0
    } else if (tipoVehiculo.equals("Auto", ignoreCase = true)) {
        tarifaHora = 4.0
    } else if (tipoVehiculo.equals("Camioneta", ignoreCase = true)) {
        tarifaHora = 10.0
    } else if (tipoVehiculo.equals("Trailer", ignoreCase = true)) {
        tarifaHora = 20.0
    }

    // CÁLCULO BASE
    var importe = tarifaHora * horas

    // RECARGO
    var porcentajeRecargo = 0.0

    if (horas > 2 && horas <= 5) {
        porcentajeRecargo = 20.0
        importe = importe + (importe * 0.20)
    } else if (horas > 5) {
        porcentajeRecargo = 50.0
        importe = importe + (importe * 0.50)
    }

    // DESCUENTO CLIENTE FRECUENTE
    var descuento = 0.0

    if (clienteFrecuente.equals("S", ignoreCase = true)) {
        descuento = importe * 0.10
        importe = importe - descuento
    }

    // MOSTRAR CÁLCULOS
    println()
    println("==========================================")
    println("              CÁLCULOS")
    println("==========================================")
    println("Tarifa por hora: S/ %.2f".format(tarifaHora))
    println("Importe inicial: S/ %.2f".format(tarifaHora * horas))
    println("Recargo: %.0f%%".format(porcentajeRecargo))
    println("Descuento: S/ %.2f".format(descuento))
    println("Total a pagar: S/ %.2f".format(importe))
}
