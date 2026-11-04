package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;

public class AdaptiveEngine {

    private final StrategySelector selector;
    private final MetricsMonitor monitor;
    private final AdaptationManager administrador;

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

        Strategy<?, ?> estrategia = selector.seleccionar(contexto);

        if (estrategia == null) {
            throw new IllegalStateException(
                    "No existe una estrategia compatible"
            );
        }

        Result<O> resultado = ejecutarEstrategia(
                estrategia,
                entrada
        );

        StrategyMetrics metricas =
                monitor.obtenerMetricas(estrategia.id());

        if (!administrador.debeCambiar(metricas)) {
            return resultado;
        }

        Strategy<?, ?> alternativa = selector.seleccionar(
                contexto,
                estrategia.id()
        );

        if (alternativa == null) {
            return resultado;
        }

        return ejecutarEstrategia(alternativa, entrada);
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