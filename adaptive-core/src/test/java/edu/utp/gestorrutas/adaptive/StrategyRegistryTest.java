package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyRegistryTest {

    static class EstrategiaPrueba implements Strategy<String, String> {

        private final String identificador;

        EstrategiaPrueba(String identificador) {
            this.identificador = identificador;
        }

        @Override
        public String id() {
            return identificador;
        }

        @Override
        public boolean supports(Context contexto) {
            return true;
        }

        @Override
        public Result<String> execute(String entrada) {
            return new Result<>(
                    true,
                    entrada,
                    "Correcto",
                    0
            );
        }
    }

    @Test
    void debeRegistrarUnaEstrategia() {
        StrategyRegistry registro = new StrategyRegistry();
        EstrategiaPrueba estrategia = new EstrategiaPrueba("prueba");

        registro.registrar(estrategia);

        assertEquals(1, registro.obtenerTodas().size());
        assertSame(estrategia, registro.buscarPorId("prueba"));
    }

    @Test
    void noDebePermitirEstrategiasDuplicadas() {
        StrategyRegistry registro = new StrategyRegistry();

        registro.registrar(new EstrategiaPrueba("prueba"));

        assertThrows(
                IllegalArgumentException.class,
                () -> registro.registrar(
                        new EstrategiaPrueba("prueba")
                )
        );
    }

    @Test
    void noDebePermitirRegistrarNulos() {
        StrategyRegistry registro = new StrategyRegistry();

        assertThrows(
                IllegalArgumentException.class,
                () -> registro.registrar(null)
        );
    }
}
