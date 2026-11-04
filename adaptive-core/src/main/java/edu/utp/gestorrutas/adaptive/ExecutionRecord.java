package edu.utp.gestorrutas.adaptive;

public record ExecutionRecord(
        String estrategia,
        long tiempoMs,
        boolean exitoso
) {

    public ExecutionRecord {

        if (estrategia == null || estrategia.isBlank()) {
            throw new IllegalArgumentException(
                    "La estrategia es obligatoria"
            );
        }

        if (tiempoMs < 0) {
            throw new IllegalArgumentException(
                    "El tiempo no puede ser negativo"
            );
        }
    }
}
