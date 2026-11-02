package edu.utp.gestorrutas.plugins.java.dijkstra;

import edu.utp.gestorrutas.domain.Graph;

/**
 * Representa una solicitud para calcular una ruta.
 *
 * @param graph grafo donde se buscará la ruta
 * @param origenId identificador del nodo de origen
 * @param destinoId identificador del nodo de destino
 */
public record RouteRequest(
        Graph graph,
        String origenId,
        String destinoId
) {
}