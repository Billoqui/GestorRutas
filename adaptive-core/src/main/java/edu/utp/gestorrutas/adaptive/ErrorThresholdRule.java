package edu.utp.gestorrutas.adaptive;

public class ErrorThresholdRule implements AdaptationRule {

    private final double porcentajeMaximoErrores;

    public ErrorThresholdRule(double porcentajeMaximoErrores) {

        if (porcentajeMaximoErrores < 0 ||
                porcentajeMaximoErrores > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje debe estar entre 0 y 100"
            );
        }

        this.porcentajeMaximoErrores = porcentajeMaximoErrores;
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

        double porcentajeErrores =
                (double) metricas.obtenerTotalErrores()
                        / metricas.obtenerTotalEjecuciones() * 100;

        return porcentajeErrores >= porcentajeMaximoErrores;
    }

    @Override
    public String obtenerMotivo() {
        return "El porcentaje de errores alcanzó el límite permitido";
    }
}
