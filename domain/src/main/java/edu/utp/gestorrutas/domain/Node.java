
package edu.utp.gestorrutas.domain;

import java.util.Objects;

/**
 * Representa un punto dentro del grafo de rutas.
 */
public class Node {

    private final String id;
    private final String name;

    public Node(String id, String name) {
        Objects.requireNonNull(id, "El ID no puede ser null");
        Objects.requireNonNull(name, "El nombre no puede ser null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        this.id = id.trim();
        this.name = name.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
