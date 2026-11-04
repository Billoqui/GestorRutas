
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyMetricsTest {

    @Test
    void debeRegistrarEjecucionesCorrectamente() {
        StrategyMetrics metricas = new StrategyMetrics();

        metricas.registrarEjecucion(
                new Result<>(true, "Ruta A-B", "Correcto", 20)
        );

        metricas.registrarEjecucion(
                new Result<>(false, null, "Error", 30)
        );

        assertEquals(2, metricas.obtenerTotalEjecuciones());
        assertEquals(1, metricas.obtenerTotalErrores());
        assertEquals(50, metricas.obtenerTiempoTotalMs());
        assertEquals(25.0, metricas.obtenerTiempoPromedioMs());
    }

    @Test
    void debeRetornarCeroCuandoNoHayEjecuciones() {
        StrategyMetrics metricas = new StrategyMetrics();

        assertEquals(0, metricas.obtenerTotalEjecuciones());
        assertEquals(0, metricas.obtenerTotalErrores());
        assertEquals(0, metricas.obtenerTiempoPromedioMs());
    }

    @Test
    void noDebeAceptarResultadosNulos() {
        StrategyMetrics metricas = new StrategyMetrics();

        assertThrows(
                IllegalArgumentException.class,
                () -> metricas.registrarEjecucion(null)
        );
    }
}
