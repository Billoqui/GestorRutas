package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.adaptive.StrategyRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstrategiasConfiguracionTest {

    @Test
    void debeCargarTresEstrategiasMedianteReflection() {

        StrategyRegistry registro =
                EstrategiasConfiguracion.crearRegistro("tiempo");

        assertEquals(3, registro.obtenerTodas().size());

        assertNotNull(registro.buscarPorId("dijkstra"));
        assertNotNull(registro.buscarPorId("astar"));
        assertNotNull(registro.buscarPorId("dijkstra-scala"));
    }

    @Test
    void debeCambiarElOrdenSegunElCriterio() {

        StrategyRegistry registro =
                EstrategiasConfiguracion.crearRegistro("scala");

        assertEquals(
                "dijkstra-scala",
                registro.obtenerTodas().get(0).id()
        );

        assertEquals(
                "dijkstra",
                registro.obtenerTodas().get(1).id()
        );

        assertEquals(
                "astar",
                registro.obtenerTodas().get(2).id()
        );
    }

    @Test
    void debeRechazarUnCriterioNoReconocido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> EstrategiasConfiguracion.crearRegistro("aleatorio")
        );
    }

    @Test
    void debeRechazarUnCriterioVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> EstrategiasConfiguracion.crearRegistro(" ")
        );
    }
}