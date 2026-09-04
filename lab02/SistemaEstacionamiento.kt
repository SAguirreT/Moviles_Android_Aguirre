fun main() {

    println("==========================================")
    println("       SISTEMA DE ESTACIONAMIENTO")
    println("==========================================")

    // DATOS DEL ESTACIONAMIENTO
    print("Ingrese el nombre del estacionamiento: ")
    val nombreEstacionamiento = readln().trim()

    print("Ingrese la cantidad de aforo: ")
    val aforoMaximo = readln().toInt()

    var vehiculosRegistrados = 0

    println()
    println("==========================================")
    println("Estacionamiento: $nombreEstacionamiento")
    println("Aforo máximo: $aforoMaximo")
    println("Cantidad de horas máximas por vehículo: 32 horas")
    println("==========================================")

    // DATOS DEL CLIENTE
    print("Ingrese el nombre del cliente: ")
    val nombreCliente = readln().trim()

    val maximoPorCliente = 5
    val resultados = mutableListOf<String>()
    var continuar = "S"

    while (
        continuar.equals("S", ignoreCase = true) &&
        vehiculosRegistrados < aforoMaximo &&
        vehiculosRegistrados < maximoPorCliente
    ) {

        println()
        println("-------- REGISTRO DE VEHÍCULO --------")

        // DATOS DEL VEHÍCULO
        print("Ingrese la placa: ")
        val placa = readln().trim()

        print("Ingrese el tipo de vehículo (Moto/Auto/Camioneta/Trailer): ")
        val tipoVehiculo = readln().trim()

        // HORAS
        var horas: Int

        while (true) {
            print("Ingrese la cantidad de horas: ")

            val entradaHoras = readln().trim()

            val horasIngresadas = entradaHoras.toLongOrNull()

            if (horasIngresadas != null &&
                horasIngresadas > 0 &&
                horasIngresadas <= 32
            ) {
                horas = horasIngresadas.toInt()
                break
            }

            println("Las horas permitidas por vehículo son máximo 32 horas.")
        }

        print("¿Es cliente frecuente? (S/N): ")
        val clienteFrecuente = readln().trim()

        // TARIFA
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

        // DESCUENTO MAYOR A S/ 500
        var descuentoMayor500 = 0.0

        if (importe > 500) {
            descuentoMayor500 = importe * 0.20
            importe -= descuentoMayor500
        }

        // IGV 18%
        val igv = importe * 0.18

        // TOTAL
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

        // CONTROL DE LÍMITES
        if (vehiculosRegistrados == maximoPorCliente) {

            println()
            println("==========================================")
            println("       LÍMITE POR CLIENTE ALCANZADO")
            println("==========================================")
            println("El cliente solo puede registrar 5 vehículos.")
            continuar = "N"

        } else if (vehiculosRegistrados == aforoMaximo) {

            println()
            println("==========================================")
            println("           AFORO ALCANZADO")
            println("==========================================")
            println("No se pueden registrar más vehículos.")
            continuar = "N"

        } else {

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
    println("Estacionamiento: $nombreEstacionamiento")
    println("Cliente: $nombreCliente")
    println()

    for (resultado in resultados) {
        println(resultado)
        println("------------------------------------------")
    }

    // AFORO
    println("Aforo ocupado    : $vehiculosRegistrados / $aforoMaximo")
    println("Aforo disponible : ${aforoMaximo - vehiculosRegistrados}")

    println("==========================================")
}
