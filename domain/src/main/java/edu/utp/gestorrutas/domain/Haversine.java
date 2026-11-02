package edu.utp.gestorrutas.domain;

/**
 * Calcula la distancia geográfica entre dos nodos
 * mediante la fórmula de Haversine.
 */
public final class Haversine {

    private static final double RADIO_TIERRA_KM = 6371.0;

    private Haversine() {
    }

    /**
     * Calcula la distancia en kilómetros entre dos nodos.
     */
    public static double calcularDistancia(Node origen, Node destino) {

        if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Los nodos no pueden ser null"
            );
        }

        if (!origen.hasCoordinates() || !destino.hasCoordinates()) {
            throw new IllegalArgumentException(
                    "Ambos nodos deben tener coordenadas"
            );
        }

        double latitud1 = Math.toRadians(origen.getLatitude());
        double latitud2 = Math.toRadians(destino.getLatitude());

        double diferenciaLatitud = Math.toRadians(
                destino.getLatitude() - origen.getLatitude()
        );

        double diferenciaLongitud = Math.toRadians(
                destino.getLongitude() - origen.getLongitude()
        );

        double a = Math.pow(Math.sin(diferenciaLatitud / 2), 2)
                + Math.cos(latitud1)
                * Math.cos(latitud2)
                * Math.pow(Math.sin(diferenciaLongitud / 2), 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return RADIO_TIERRA_KM * c;
    }
}