
package edu.utp.gestorrutas.domain;

import java.util.Objects;

/**
 * Representa una conexión dirigida entre dos nodos del grafo.
 */
public class Connection {

    private final Node origen;
    private final Node destino;
    private final double peso;

    public Connection(Node origen, Node destino, double peso) {
        this.origen = Objects.requireNonNull(
                origen, "El nodo de origen no puede ser null"
        );

        this.destino = Objects.requireNonNull(
                destino, "El nodo de destino no puede ser null"
        );

        if (!Double.isFinite(peso) || peso < 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser un número finito y no negativo"
            );
        }

        this.peso = peso;
    }

    public Node getOrigen() {
        return origen;
    }

    public Node getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }
}
