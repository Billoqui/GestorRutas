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
                        "EjecuciÃƒÆ’Ã‚Â³n correcta",
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
        assertEquals(1, motor.obtenerHistorial().obtenerTodas().size());

        AdaptationDecision decision =
                motor.obtenerHistorial().obtenerTodas().get(0);

        assertEquals("prueba", decision.estrategiaInicial());
        assertFalse(decision.huboCambio());
        assertTrue(decision.motivos().isEmpty());
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
                        "EjecuciÃƒÆ’Ã‚Â³n completada",
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
                        "Resultado rÃƒÆ’Ã‚Â¡pido",
                        "EjecuciÃƒÆ’Ã‚Â³n completada",
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
        assertEquals("Resultado rÃƒÆ’Ã‚Â¡pido", resultado.valor());

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
        assertEquals(1, motor.obtenerHistorial().obtenerTodas().size());

        AdaptationDecision decision =
                motor.obtenerHistorial().obtenerTodas().get(0);

        assertEquals("estrategia-lenta", decision.estrategiaInicial());
        assertTrue(decision.huboCambio());
        assertEquals("estrategia-rapida", decision.estrategiaAlternativa());

        assertFalse(decision.motivos().isEmpty());
        assertEquals(
                "El tiempo promedio de ejecución superó el límite permitido",
                decision.motivos().get(0)
        );
    }
    @Test
    void debeRegistrarCuandoNoExisteEstrategiaAlternativa() {

        Strategy<String, String> estrategia = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-unica";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        "Resultado original",
                        "EjecuciÃƒÆ’Ã‚Â³n completada",
                        150
                );
            }
        };

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(estrategia);

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
        assertEquals("Resultado original", resultado.valor());

        AdaptationDecision decision =
                motor.obtenerHistorial().obtenerTodas().get(0);

        assertEquals("estrategia-unica", decision.estrategiaInicial());
        assertFalse(decision.huboCambio());
        assertNull(decision.estrategiaAlternativa());
        assertFalse(decision.motivos().isEmpty());
    }


    @Test
    void debeProbarTresEstrategiasConsecutivas() {

        Strategy<String, String> primera = crearEstrategiaLenta(
                "estrategia-primera",
                "Resultado primero"
        );

        Strategy<String, String> segunda = crearEstrategiaLenta(
                "estrategia-segunda",
                "Resultado segundo"
        );

        Strategy<String, String> tercera = crearEstrategiaLenta(
                "estrategia-tercera",
                "Resultado tercero"
        );

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(primera);
        registro.registrar(segunda);
        registro.registrar(tercera);

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
        assertEquals("Resultado tercero", resultado.valor());

        assertEquals(1, monitor.obtenerMetricas("estrategia-primera")
                .obtenerTotalEjecuciones());

        assertEquals(1, monitor.obtenerMetricas("estrategia-segunda")
                .obtenerTotalEjecuciones());

        assertEquals(1, monitor.obtenerMetricas("estrategia-tercera")
                .obtenerTotalEjecuciones());

        AdaptationDecision decision =
                motor.obtenerHistorial().obtenerTodas().get(0);

        assertEquals("estrategia-primera", decision.estrategiaInicial());
        assertEquals("estrategia-tercera", decision.estrategiaAlternativa());
        assertTrue(decision.huboCambio());
        assertEquals(3, decision.motivos().size());
    }

    @Test
    void debeCambiarDeEstrategiaCuandoLaPrimeraFalla() {

        Strategy<String, String> fallida = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-fallida";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        false,
                        null,
                        "No se pudo ejecutar la estrategia",
                        10
                );
            }
        };

        Strategy<String, String> alternativa = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-alternativa";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        "Resultado correcto",
                        "Ejecución completada",
                        15
                );
            }
        };

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(fallida);
        registro.registrar(alternativa);

        MetricsMonitor monitor = new MetricsMonitor();
        AdaptationManager administrador = new AdaptationManager();

        AdaptiveEngine motor = new AdaptiveEngine(
                new StrategySelector(registro),
                monitor,
                administrador
        );

        Result<String> resultado = motor.ejecutar(
                new Context("A", "B", "distancia", false),
                "entrada"
        );

        assertTrue(resultado.exitoso());
        assertEquals("Resultado correcto", resultado.valor());

        assertEquals(1, monitor.obtenerMetricas("estrategia-fallida")
                .obtenerTotalEjecuciones());

        assertEquals(1, monitor.obtenerMetricas("estrategia-alternativa")
                .obtenerTotalEjecuciones());
    }

    private Strategy<String, String> crearEstrategiaLenta(
            String id,
            String valor
    ) {
        return new Strategy<>() {
            @Override
            public String id() {
                return id;
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        valor,
                        "EjecuciÃƒÆ’Ã‚Â³n completada",
                        150
                );
            }
        };
    }

    @Test
    void debeCambiarDeEstrategiaCuandoSeSuperaElPorcentajeDeErrores() {

        Strategy<String, String> estrategiaFallida = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-con-errores";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        false,
                        null,
                        "No se pudo encontrar la ruta",
                        10
                );
            }
        };

        Strategy<String, String> estrategiaAlternativa = new Strategy<>() {
            @Override
            public String id() {
                return "estrategia-alternativa";
            }

            @Override
            public boolean supports(Context contexto) {
                return true;
            }

            @Override
            public Result<String> execute(String entrada) {
                return new Result<>(
                        true,
                        "Ruta encontrada",
                        "Ejecución completada",
                        15
                );
            }
        };

        StrategyRegistry registro = new StrategyRegistry();
        registro.registrar(estrategiaFallida);
        registro.registrar(estrategiaAlternativa);

        MetricsMonitor monitor = new MetricsMonitor();

        AdaptationManager administrador = new AdaptationManager();
        administrador.agregarRegla(new ErrorThresholdRule(30));

        AdaptiveEngine motor = new AdaptiveEngine(
                new StrategySelector(registro),
                monitor,
                administrador
        );

        Result<String> resultado = motor.ejecutar(
                new Context("A", "B", "distancia", false),
                "entrada"
        );

        assertTrue(resultado.exitoso());
        assertEquals("Ruta encontrada", resultado.valor());

        AdaptationDecision decision =
                motor.obtenerHistorial().obtenerTodas().get(0);

        assertEquals("estrategia-con-errores", decision.estrategiaInicial());
        assertEquals("estrategia-alternativa", decision.estrategiaAlternativa());
        assertTrue(decision.huboCambio());

        assertTrue(decision.motivos().contains(
                "El porcentaje de errores alcanzó el límite permitido"
        ));

        assertTrue(decision.motivos().contains(
                "La estrategia estrategia-con-errores no pudo completar la ejecución"
        ));
    }
}
