package edu.utp.gestorrutas.api;
//Representa las condiciones de una solicitud de ruta
//No calcula nada
//Simplemente almacena información que los algoritmos necesitan para evaluar si pueden resolverla
public record Context(
        String origen,
        String destino,
        String criterio,
        boolean permitePesosNegativos
) {
}
