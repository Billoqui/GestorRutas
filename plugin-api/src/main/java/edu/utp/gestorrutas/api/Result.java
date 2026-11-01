package edu.utp.gestorrutas.api;

// Cuando un algoritmo termina debemos saber:
// Encontro una ruta?
// Informacion sobre lo que ocurrio
// Tiempo de ejecucion

public record Result<T>(
        boolean exitoso,
        T valor,
        String mensaje,
        long tiempoEjecucionMs
) {
}