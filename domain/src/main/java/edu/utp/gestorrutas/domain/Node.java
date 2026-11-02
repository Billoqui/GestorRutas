package edu.utp.gestorrutas.domain;

import java.util.Objects;

/**
 * Representa un punto dentro del grafo de rutas.
 */
public class Node {

    private final String id;
    private final String name;
    private final double latitude;
    private final double longitude;

    /**
     * Constructor para nodos sin coordenadas geográficas.
     */
    public Node(String id, String name) {
        this(id, name, Double.NaN, Double.NaN);
    }

    /**
     * Constructor para nodos con coordenadas geográficas.
     */
    public Node(String id, String name, double latitude, double longitude) {
        Objects.requireNonNull(id, "El ID no puede ser null");
        Objects.requireNonNull(name, "El nombre no puede ser null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (!Double.isNaN(latitude)
                && (!Double.isFinite(latitude) || latitude < -90 || latitude > 90)) {
            throw new IllegalArgumentException("La latitud no es válida");
        }

        if (!Double.isNaN(longitude)
                && (!Double.isFinite(longitude) || longitude < -180 || longitude > 180)) {
            throw new IllegalArgumentException("La longitud no es válida");
        }

        if (Double.isNaN(latitude) != Double.isNaN(longitude)) {
            throw new IllegalArgumentException(
                    "Ambas coordenadas deben estar presentes o ausentes"
            );
        }

        this.id = id.trim();
        this.name = name.trim();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean hasCoordinates() {
        return !Double.isNaN(latitude) && !Double.isNaN(longitude);
    }
}