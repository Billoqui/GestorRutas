
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategySelectorTest {

    static class EstrategiaPrueba implements Strategy<String, String> {

        private final String identificador;
        private final boolean compatible;

        EstrategiaPrueba(String identificador, boolean compatible) {
            this.identificador = identificador;
            this.compatible = compatible;
        }

        @Override
        public String id() {
            return identificador;
        }

        @Override
        public boolean supports(Context contexto) {
            return compatible;
        }

        @Override
        public Result<String> execute(String entrada) {
            return new Result<>(true, entrada, "Correcto", 0);
        }
    }

    @Test
    void debeSeleccionarLaEstrategiaCompatible() {
        StrategyRegistry registro = new StrategyRegistry();

        EstrategiaPrueba incompatible =
                new EstrategiaPrueba("incompatible", false);

        EstrategiaPrueba compatible =
                new EstrategiaPrueba("compatible", true);

        registro.registrar(incompatible);
        registro.registrar(compatible);

        StrategySelector selector = new StrategySelector(registro);

        Strategy<?, ?> seleccionada =
                selector.seleccionar(new Context(
                        "A", "B", "distancia", false
                ));

        assertSame(compatible, seleccionada);
    }

    @Test
    void debeRetornarNuloSiNingunaEsCompatible() {
        StrategyRegistry registro = new StrategyRegistry();

        registro.registrar(
                new EstrategiaPrueba("incompatible", false)
        );

        StrategySelector selector = new StrategySelector(registro);

        assertNull(selector.seleccionar(new Context(
                "A", "B", "distancia", false
        )));
    }

    @Test
    void noDebeAceptarContextoNulo() {
        StrategyRegistry registro = new StrategyRegistry();
        StrategySelector selector = new StrategySelector(registro);

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.seleccionar(null)
        );
    }
}
