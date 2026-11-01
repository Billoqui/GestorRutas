package edu.utp.gestorrutas.domain;

/**
 * Representa un punto dentro del grafo de rutas.
 */
public class Node {

    private final String id;
    private final String name;

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}