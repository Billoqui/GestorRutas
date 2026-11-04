
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;

import java.util.HashMap;
import java.util.Map;

public class MetricsMonitor {

    private final Map<String, StrategyMetrics> metricas = new HashMap<>();

    public void registrarResultado(String estrategiaId, Result<?> resultado) {

        if (estrategiaId == null || estrategiaId.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID de la estrategia no es válido"
            );
        }

        metricas.putIfAbsent(estrategiaId, new StrategyMetrics());

        metricas.get(estrategiaId).registrarEjecucion(resultado);
    }

    public StrategyMetrics obtenerMetricas(String estrategiaId) {
        return metricas.get(estrategiaId);
    }

    public Map<String, StrategyMetrics> obtenerTodasLasMetricas() {
        return Map.copyOf(metricas);
    }
}
