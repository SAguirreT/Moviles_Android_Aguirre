fun main() {

    println("==========================================")
    println("       SISTEMA DE ESTACIONAMIENTO")
    println("==========================================")

    print("Ingrese la placa: ")
    val placa = readln()

    print("Ingrese el tipo de vehículo (Moto/Auto/Camioneta): ")
    val tipoVehiculo = readln()

    print("Ingrese la cantidad de horas: ")
    val horas = readln().toInt()

    print("¿Es cliente frecuente? (S/N): ")
    val clienteFrecuente = readln()

    println()
    println("--------- DATOS INGRESADOS ---------")
    println("Placa: $placa")
    println("Tipo de vehículo: $tipoVehiculo")
    println("Horas: $horas")
    println("Cliente frecuente: $clienteFrecuente")
}