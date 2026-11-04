package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdaptationManagerTest {

    @Test
    void debeCambiarCuandoUnaReglaSeCumple() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 150)
        );

        AdaptationManager administrador = new AdaptationManager();

        administrador.agregarRegla(new ErrorThresholdRule(30));
        administrador.agregarRegla(new ExecutionTimeRule(100));

        assertTrue(administrador.debeCambiar(metricas));
    }

    @Test
    void noDebeCambiarCuandoNingunaReglaSeCumple() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 50)
        );

        AdaptationManager administrador = new AdaptationManager();

        administrador.agregarRegla(new ErrorThresholdRule(30));
        administrador.agregarRegla(new ExecutionTimeRule(100));

        assertFalse(administrador.debeCambiar(metricas));
    }

    @Test
    void debeObtenerLosMotivosDelCambio() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "A-B", "Correcto", 150)
        );

        AdaptationManager administrador = new AdaptationManager();

        administrador.agregarRegla(new ExecutionTimeRule(100));

        assertEquals(1, administrador.obtenerMotivos(metricas).size());
    }

    @Test
    void noDebePermitirReglasNulas() {
        AdaptationManager administrador = new AdaptationManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> administrador.agregarRegla(null)
        );
    }
}
