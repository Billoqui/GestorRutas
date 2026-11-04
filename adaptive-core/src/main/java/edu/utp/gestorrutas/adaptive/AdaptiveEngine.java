
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;

import java.util.List;

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

        List<String> motivos =
                administrador.obtenerMotivos(metricas);

        if (motivos.isEmpty()) {
            historial.registrar(new AdaptationDecision(
                    estrategia.id(),
                    motivos,
                    null
            ));

            return resultado;
        }

        Strategy<?, ?> alternativa = selector.seleccionar(
                contexto,
                estrategia.id()
        );

        if (alternativa == null) {
            historial.registrar(new AdaptationDecision(
                    estrategia.id(),
                    motivos,
                    null
            ));

            return resultado;
        }

        historial.registrar(new AdaptationDecision(
                estrategia.id(),
                motivos,
                alternativa.id()
        ));

        return ejecutarEstrategia(alternativa, entrada);
    }

    public AdaptationHistory obtenerHistorial() {
        return historial;
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
