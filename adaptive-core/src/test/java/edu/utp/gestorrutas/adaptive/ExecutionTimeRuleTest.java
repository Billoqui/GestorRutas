package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionTimeRuleTest {

    @Test
    void debeRecomendarCambioCuandoSuperaElLimite() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 120)
        );

        ExecutionTimeRule regla = new ExecutionTimeRule(100);

        assertTrue(regla.debeCambiar(metricas));
    }

    @Test
    void noDebeRecomendarCambioCuandoEstaDebajoDelLimite() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 80)
        );

        ExecutionTimeRule regla = new ExecutionTimeRule(100);

        assertFalse(regla.debeCambiar(metricas));
    }

    @Test
    void debeRecomendarCambioCuandoAlcanzaElLimite() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 100)
        );

        ExecutionTimeRule regla = new ExecutionTimeRule(100);

        assertTrue(regla.debeCambiar(metricas));
    }

    @Test
    void noDebeRecomendarCambioSinEjecuciones() {
        StrategyMetrics metricas = new StrategyMetrics();

        ExecutionTimeRule regla = new ExecutionTimeRule(100);

        assertFalse(regla.debeCambiar(metricas));
    }

    @Test
    void debeRechazarTiempoMaximoNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutionTimeRule(-1)
        );
    }

    @Test
    void debeRechazarMetricasNulas() {
        ExecutionTimeRule regla = new ExecutionTimeRule(100);

        assertThrows(
                IllegalArgumentException.class,
                () -> regla.debeCambiar(null)
        );
    }
}
