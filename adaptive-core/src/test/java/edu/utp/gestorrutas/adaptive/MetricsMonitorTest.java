
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsMonitorTest {

    @Test
    void debeRegistrarMetricasPorEstrategia() {
        MetricsMonitor monitor = new MetricsMonitor();

        monitor.registrarResultado(
                "dijkstra",
                new Result<>(true, "A-B-C", "Correcto", 20)
        );

        monitor.registrarResultado(
                "astar",
                new Result<>(false, null, "Error", 30)
        );

        assertEquals(
                1,
                monitor.obtenerMetricas("dijkstra")
                        .obtenerTotalEjecuciones()
        );

        assertEquals(
                0,
                monitor.obtenerMetricas("dijkstra")
                        .obtenerTotalErrores()
        );

        assertEquals(
                1,
                monitor.obtenerMetricas("astar")
                        .obtenerTotalErrores()
        );
    }

    @Test
    void debeRetornarNuloParaUnaEstrategiaNoRegistrada() {
        MetricsMonitor monitor = new MetricsMonitor();

        assertNull(monitor.obtenerMetricas("inexistente"));
    }

    @Test
    void debeRechazarUnIdentificadorInvalido() {
        MetricsMonitor monitor = new MetricsMonitor();

        assertThrows(
                IllegalArgumentException.class,
                () -> monitor.registrarResultado(
                        "",
                        new Result<>(true, "A-B", "Correcto", 10)
                )
        );
    }

    @Test
    void debeRechazarUnResultadoNulo() {
        MetricsMonitor monitor = new MetricsMonitor();

        assertThrows(
                IllegalArgumentException.class,
                () -> monitor.registrarResultado("dijkstra", null)
        );
    }
}
