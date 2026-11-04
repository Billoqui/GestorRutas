package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsMonitor {

    private final Map<String, StrategyMetrics> metricas = new HashMap<>();

    private final List<ExecutionRecord> ejecuciones = new ArrayList<>();

    public void registrarResultado(
            String estrategiaId,
            Result<?> resultado
    ) {

        if (estrategiaId == null || estrategiaId.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID de la estrategia no es valido"
            );
        }

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "El resultado no puede ser nulo"
            );
        }

        metricas.putIfAbsent(estrategiaId, new StrategyMetrics());

        metricas.get(estrategiaId).registrarEjecucion(resultado);

        ejecuciones.add(new ExecutionRecord(
                estrategiaId,
                resultado.tiempoEjecucionMs(),
                resultado.exitoso()
        ));
    }

    public StrategyMetrics obtenerMetricas(String estrategiaId) {
        return metricas.get(estrategiaId);
    }

    public Map<String, StrategyMetrics> obtenerTodasLasMetricas() {
        return Map.copyOf(metricas);
    }

    public List<ExecutionRecord> obtenerEjecuciones() {
        return List.copyOf(ejecuciones);
    }
}

