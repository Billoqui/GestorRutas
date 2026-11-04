package edu.utp.gestorrutas.adaptive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdaptiveConfigLoaderTest {

    @Test
    void debeCargarConfiguracionDesdeJson() {

        AdaptiveConfigLoader loader = new AdaptiveConfigLoader();

        AdaptiveConfig configuracion = loader.cargar();

        assertEquals(100, configuracion.getTiempoMaximoMs());
        assertTrue(configuracion.isCambiarSiSuperaLimite());
    }
}