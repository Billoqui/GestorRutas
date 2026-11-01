
package edu.utp.gestorrutas.api;

/**
 * Representa las condiciones bajo las cuales
 * se debe calcular una ruta.
 *
 * Es inmutable para evitar modificaciones
 * inesperadas durante la ejecución.
 *
 * @param origen identificador del punto de partida
 * @param destino identificador del punto de llegada
 * @param criterio criterio de optimización (distancia, tiempo o costo)
 * @param permitePesosNegativos indica si se permiten conexiones con pesos negativos
 */
public record Context(
        String origen,
        String destino,
        String criterio,
        boolean permitePesosNegativos
) {
}
