package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdaptiveEngineTest {

    @Test
    void debeEjecutarEstrategiaCompatible() {
        Strategy<String, String> estrategia = new Strategy<>() {
            @Override
            public String id() {
                return "prueba";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        entrada.toUpperCase(),
                        "Ejecución correcta",
                        10
                );
            }
        };

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(estrategia);

        StrategySelector selector = new StrategySelector(registro);
        MetricsMonitor monitor = new MetricsMonitor();
        AdaptationManager administrador = new AdaptationManager();

        AdaptiveEngine motor = new AdaptiveEngine(
                selector,
                monitor,
                administrador
        );

        Result<String> resultado = motor.ejecutar(
                new Context("A", "B", "distancia", false),
                "hola"
        );

        assertTrue(resultado.exitoso());
        assertEquals("HOLA", resultado.valor());
        assertEquals(1, monitor.obtenerMetricas("prueba")
                .obtenerTotalEjecuciones());
    }

    @Test
    void debeRechazarContextoNulo() {
        AdaptiveEngine motor = crearMotor();

        assertThrows(
                IllegalArgumentException.class,
                () -> motor.ejecutar(null, "entrada")
        );
    }

    @Test
    void debeRechazarEntradaNula() {
        AdaptiveEngine motor = crearMotor();

        assertThrows(
                IllegalArgumentException.class,
                () -> motor.ejecutar(
                        new Context("A", "B", "distancia", false),
                        null
                )
        );
    }

    @Test
    void debeRechazarSiNoExisteEstrategiaCompatible() {
        AdaptiveEngine motor = crearMotor();

        assertThrows(
                IllegalStateException.class,
                () -> motor.ejecutar(
                        new Context("A", "B", "distancia", false),
                        "entrada"
                )
        );
    }

    private AdaptiveEngine crearMotor() {
        return new AdaptiveEngine(
                new StrategySelector(new StrategyRegistry()),
                new MetricsMonitor(),
                new AdaptationManager()
        );
    }
    @Test
    void debeCambiarDeEstrategiaCuandoSeSuperaElTiempoPermitido() {

        Strategy<String, String> lenta = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-lenta";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        "Resultado lento",
                        "Ejecución completada",
                        150
                );
            }
        };

        Strategy<String, String> rapida = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-rapida";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        "Resultado rápido",
                        "Ejecución completada",
                        10
                );
            }
        };

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(lenta);
        registro.registrar(rapida);

        StrategySelector selector = new StrategySelector(registro);
        MetricsMonitor monitor = new MetricsMonitor();

        AdaptationManager administrador = new AdaptationManager();
        administrador.agregarRegla(new ExecutionTimeRule(100));

        AdaptiveEngine motor = new AdaptiveEngine(
                selector,
                monitor,
                administrador
        );

        Result<String> resultado = motor.ejecutar(
                new Context("A", "B", "distancia", false),
                "entrada"
        );

        assertTrue(resultado.exitoso());
        assertEquals("Resultado rápido", resultado.valor());

        assertEquals(
                1,
                monitor.obtenerMetricas("estrategia-lenta")
                        .obtenerTotalEjecuciones()
        );

        assertEquals(
                1,
                monitor.obtenerMetricas("estrategia-rapida")
                        .obtenerTotalEjecuciones()
        );
    }
}