package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorThresholdRuleTest {

    @Test
    void debeRecomendarCambioCuandoAlcanzaElLimite() {
        StrategyMetrics metricas = new StrategyMetrics();

        for (int i = 0; i < 10; i++) {
            boolean exitoso = i < 7;

            metricas.registrarEjecucion(
                    new Result<>(exitoso, null, "Prueba", 10)
            );
        }

        ErrorThresholdRule regla = new ErrorThresholdRule(30);

        assertTrue(regla.debeCambiar(metricas));
    }

    @Test
    void noDebeRecomendarCambioCuandoNoAlcanzaElLimite() {
        StrategyMetrics metricas = new StrategyMetrics();

        for (int i = 0; i < 10; i++) {
            boolean exitoso = i < 9;

            metricas.registrarEjecucion(
                    new Result<>(exitoso, null, "Prueba", 10)
            );
        }

        ErrorThresholdRule regla = new ErrorThresholdRule(30);

        assertFalse(regla.debeCambiar(metricas));
    }

    @Test
    void noDebeRecomendarCambioSinEjecuciones() {
        StrategyMetrics metricas = new StrategyMetrics();

        ErrorThresholdRule regla = new ErrorThresholdRule(30);

        assertFalse(regla.debeCambiar(metricas));
    }

    @Test
    void debeRechazarPorcentajesInvalidos() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ErrorThresholdRule(-1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ErrorThresholdRule(101)
        );
    }

    @Test
    void debeRechazarMetricasNulas() {
        ErrorThresholdRule regla = new ErrorThresholdRule(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> regla.debeCambiar(null)
        );
    }
}
