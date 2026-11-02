package edu.utp.gestorrutas.plugins.java.common;

import java.util.List;

/**
 * Representa una ruta encontrada entre dos nodos.
 *
 * @param nodeIds identificadores de los nodos que forman la ruta
 * @param totalWeight peso total de la ruta
 */
public record Route(
        List<String> nodeIds,
        double totalWeight
) {
}