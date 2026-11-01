
package edu.utp.gestorrutas.api;

/**
 * Representa el resultado de ejecutar una estrategia.
 *
 * @param <T> tipo de dato que contiene el resultado
 * @param exitoso indica si la ejecución terminó correctamente
 * @param valor información devuelta por la estrategia
 * @param mensaje descripción del resultado o de un posible error
 * @param tiempoEjecucionMs tiempo empleado en la ejecución, en milisegundos
 */
public record Result<T>(
        boolean exitoso,
        T valor,
        String mensaje,
        long tiempoEjecucionMs
) {
}
