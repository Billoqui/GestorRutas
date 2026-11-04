
package edu.utp.gestorrutas.adaptive;

public class ExecutionTimeRule implements AdaptationRule {

    private final double tiempoMaximoMs;

    public ExecutionTimeRule(double tiempoMaximoMs) {

        if (tiempoMaximoMs < 0) {
            throw new IllegalArgumentException(
                    "El tiempo máximo no puede ser negativo"
            );
        }

        this.tiempoMaximoMs = tiempoMaximoMs;
    }

    @Override
    public boolean debeCambiar(StrategyMetrics metricas) {

        if (metricas == null) {
            throw new IllegalArgumentException(
                    "Las métricas no pueden ser nulas"
            );
        }

        if (metricas.obtenerTotalEjecuciones() == 0) {
            return false;
        }

        return metricas.obtenerTiempoPromedioMs() >= tiempoMaximoMs;
    }

    @Override
    public String obtenerMotivo() {
        return "El tiempo promedio de ejecución superó el límite permitido";
    }
}

