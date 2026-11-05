package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdaptiveEngine {

    private final StrategySelector selector;
    private final MetricsMonitor monitor;
    private final AdaptationManager administrador;
    private final AdaptationHistory historial;

    public AdaptiveEngine(
            StrategySelector selector,
            MetricsMonitor monitor,
            AdaptationManager administrador
    ) {
        if (selector == null || monitor == null || administrador == null) {
            throw new IllegalArgumentException(
                    "Los componentes del motor son obligatorios"
            );
        }

        this.selector = selector;
        this.monitor = monitor;
        this.administrador = administrador;
        this.historial = new AdaptationHistory();
    }

    public <I, O> Result<O> ejecutar(
            Context contexto,
            I entrada
    ) {
        if (contexto == null || entrada == null) {
            throw new IllegalArgumentException(
                    "El contexto y la entrada son obligatorios"
            );
        }

        Strategy<?, ?> actual = selector.seleccionar(contexto);

        if (actual == null) {
            throw new IllegalStateException(
                    "No existe una estrategia compatible"
            );
        }

        String estrategiaInicial = actual.id();

        Set<String> estrategiasEjecutadas = new HashSet<>();
        List<String> motivosAcumulados = new ArrayList<>();

        Result<O> resultado;

        while (true) {

            estrategiasEjecutadas.add(actual.id());

            resultado = ejecutarEstrategia(actual, entrada);

            StrategyMetrics metricas =
                    monitor.obtenerMetricas(actual.id());

            List<String> motivos =
                    administrador.obtenerMotivos(metricas);

            if (resultado.exitoso() && motivos.isEmpty()) {
                break;
            }

            motivosAcumulados.addAll(motivos);

            if (!resultado.exitoso()) {
                motivosAcumulados.add(
                        "La estrategia " + actual.id()
                                + " no pudo completar la ejecución"
                );
            }

            Strategy<?, ?> alternativa =
                    selector.seleccionarExcluyendo(
                            contexto,
                            estrategiasEjecutadas
                    );

            if (alternativa == null) {
                break;
            }

            actual = alternativa;
        }

        String estrategiaFinal = actual.id();

        historial.registrar(new AdaptationDecision(
                estrategiaInicial,
                motivosAcumulados,
                estrategiaInicial.equals(estrategiaFinal)
                        ? null
                        : estrategiaFinal
        ));

        return resultado;
    }

    public AdaptationHistory obtenerHistorial() {
        return historial;
    }

    public List<ExecutionRecord> obtenerEjecuciones() {
        return monitor.obtenerEjecuciones();
    }

    private <I, O> Result<O> ejecutarEstrategia(
            Strategy<?, ?> estrategia,
            I entrada
    ) {
        @SuppressWarnings("unchecked")
        Strategy<I, O> estrategiaCompatible =
                (Strategy<I, O>) estrategia;

        Result<O> resultado = estrategiaCompatible.execute(entrada);

        monitor.registrarResultado(estrategia.id(), resultado);

        return resultado;
    }
}
