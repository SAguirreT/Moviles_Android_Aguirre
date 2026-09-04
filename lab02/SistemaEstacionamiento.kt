fun main() {

    println("==========================================")
    println("       SISTEMA DE ESTACIONAMIENTO")
    println("==========================================")

    val aforoMaximo = 30
    var vehiculosRegistrados = 0

    print("Ingrese el nombre del cliente: ")
    val nombreCliente = readln().trim()

    val resultados = mutableListOf<String>()
    var continuar = "S"

    while (continuar.equals("S", ignoreCase = true) &&
        vehiculosRegistrados < aforoMaximo) {

        // DATOS DEL VEHÍCULO
        print("Ingrese la placa: ")
        val placa = readln().trim()

        print("Ingrese el tipo de vehículo (Moto/Auto/Camioneta/Trailer): ")
        val tipoVehiculo = readln().trim()

        print("Ingrese la cantidad de horas: ")
        val horas = readln().toInt()

        print("¿Es cliente frecuente? (S/N): ")
        val clienteFrecuente = readln().trim()

        // TARIFA POR TIPO DE VEHÍCULO
        var tarifaHora = 0.0

        if (tipoVehiculo.equals("Moto", ignoreCase = true)) {
            tarifaHora = 2.0
        } else if (
            tipoVehiculo.equals("Auto", ignoreCase = true) ||
            tipoVehiculo.equals("Vehiculo", ignoreCase = true)
        ) {
            tarifaHora = 4.0
        } else if (tipoVehiculo.equals("Camioneta", ignoreCase = true)) {
            tarifaHora = 10.0
        } else if (tipoVehiculo.equals("Trailer", ignoreCase = true)) {
            tarifaHora = 20.0
        }

        // IMPORTE INICIAL
        val importeInicial = tarifaHora * horas
        var importe = importeInicial

        // RECARGO POR HORAS
        var porcentajeRecargo = 0.0

        if (horas <= 2) {
            porcentajeRecargo = 0.0
        } else if (horas <= 5) {
            porcentajeRecargo = 20.0
            importe += importe * 0.20
        } else if (horas <= 10) {
            porcentajeRecargo = 40.0
            importe += importe * 0.40
        } else {
            porcentajeRecargo = 50.0
            importe += importe * 0.50
        }

        // DESCUENTO CLIENTE FRECUENTE
        var descuentoFrecuente = 0.0

        if (clienteFrecuente.equals("S", ignoreCase = true)) {
            descuentoFrecuente = importe * 0.10
            importe -= descuentoFrecuente
        }

        // DESCUENTO DEL 20% SI SUPERA S/ 500
        var descuentoMayor500 = 0.0

        if (importe > 500) {
            descuentoMayor500 = importe * 0.20
            importe -= descuentoMayor500
        }

        // IGV 18%
        val igv = importe * 0.18

        // TOTAL FINAL
        val totalPagar = importe + igv

        vehiculosRegistrados++

        // GUARDAR RESULTADO
        resultados.add(
            """
            Placa                 : $placa
            Tipo de vehículo      : $tipoVehiculo
            Horas                 : $horas
            Tarifa por hora       : S/ %.2f
            Importe inicial       : S/ %.2f
            Recargo               : %.0f%%
            Descuento frecuente   : S/ %.2f
            Descuento > S/500     : S/ %.2f
            Subtotal              : S/ %.2f
            IGV (18%%)             : S/ %.2f
            TOTAL A PAGAR         : S/ %.2f
            """.trimIndent().format(
                tarifaHora,
                importeInicial,
                porcentajeRecargo,
                descuentoFrecuente,
                descuentoMayor500,
                importe,
                igv,
                totalPagar
            )
        )

        // PREGUNTAR SI DESEA AGREGAR OTRO VEHÍCULO
        if (vehiculosRegistrados < aforoMaximo) {
            println()
            print("¿Desea agregar otro vehículo? (S/N): ")
            continuar = readln().trim()
        }
    }

    // RESULTADO FINAL
    println()
    println("==========================================")
    println("           RESULTADO FINAL")
    println("==========================================")
    println("Cliente: $nombreCliente")
    println()

    for (resultado in resultados) {
        println(resultado)
        println("------------------------------------------")
    }

    // AFORO
    println("Aforo ocupado    : $vehiculosRegistrados / $aforoMaximo")
    println("Aforo disponible : ${aforoMaximo - vehiculosRegistrados}")

    // MENSAJE SI LLEGA AL MÁXIMO
    if (vehiculosRegistrados == aforoMaximo) {
        println()
        println("==========================================")
        println("           AFORO ALCANZADO")
        println("==========================================")
        println("No se pueden registrar más vehículos.")
    }

    println("==========================================")
}