package edu.utp.gestorrutas.domain;

/**
 * Representa una conexión entre dos nodos del grafo.
 */
public class Connection {

    private final Node origen;
    private final Node destino;
    private final double peso;

    public Connection(Node origen, Node destino, double peso) {
        this.origen = origen;
        this.destino = destino;
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