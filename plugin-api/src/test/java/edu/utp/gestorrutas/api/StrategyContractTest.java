
package edu.utp.gestorrutas.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyContractTest {

    private final Strategy<String, String> estrategia = new Strategy<>() {

        @Override
        public String id() {
            return "estrategia-prueba";
        }

        @Override
        public boolean supports(Context contexto) {
            return contexto.criterio().equals("distancia");
        }

        @Override
        public Result<String> execute(String entrada) {
            return new Result<>(
                    true,
                    entrada.toUpperCase(),
                    "Ejecución correcta",
                    5
            );
        }
    };

    @Test
    void estrategiaDebeIdentificarseCorrectamente() {
        assertEquals("estrategia-prueba", estrategia.id());
    }

    @Test
    void estrategiaDebeAceptarContextoCompatible() {
        Context contexto = new Context("A", "B", "distancia", false);

        assertTrue(estrategia.supports(contexto));
    }

    @Test
    void estrategiaDebeRechazarContextoNoCompatible() {
        Context contexto = new Context("A", "B", "tiempo", false);

        assertFalse(estrategia.supports(contexto));
    }

    @Test
    void estrategiaDebeEjecutarYDevolverResultado() {
        Result<String> resultado = estrategia.execute("ruta");

        assertTrue(resultado.exitoso());
        assertEquals("RUTA", resultado.valor());
        assertEquals("Ejecución correcta", resultado.mensaje());
        assertEquals(5, resultado.tiempoEjecucionMs());
    }
}
