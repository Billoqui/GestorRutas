
package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.domain.Node;

import edu.utp.gestorrutas.adaptive.AdaptationManager;
import edu.utp.gestorrutas.adaptive.AdaptiveEngine;
import edu.utp.gestorrutas.adaptive.ExecutionTimeRule;
import edu.utp.gestorrutas.adaptive.MetricsMonitor;
import edu.utp.gestorrutas.adaptive.StrategySelector;
import edu.utp.gestorrutas.adaptive.StrategyRegistry;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;

import edu.utp.gestorrutas.plugins.java.common.Route;
import edu.utp.gestorrutas.plugins.java.common.RouteRequest;

import edu.utp.gestorrutas.adaptive.AdaptiveConfig;
import edu.utp.gestorrutas.adaptive.AdaptiveConfigLoader;

import edu.utp.gestorrutas.persistence.DatabaseConnection;
import edu.utp.gestorrutas.persistence.AdaptationDecisionRepository;

import edu.utp.gestorrutas.persistence.ExecutionRepository;
import edu.utp.gestorrutas.adaptive.ErrorThresholdRule;

public class GestorRutasApp {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       GESTOR DE RUTAS");
        System.out.println("================================");

        Graph grafo = crearGrafo();

        DatabaseConnection conexionBD = new DatabaseConnection();

        AdaptationDecisionRepository repositorioDecisiones = new AdaptationDecisionRepository(conexionBD);

	    ExecutionRepository repositorioEjecuciones = new ExecutionRepository(conexionBD);

        StrategyRegistry registro = EstrategiasConfiguracion.crearRegistro("tiempo");
        StrategySelector selector = new StrategySelector(registro);

        RouteRequest solicitud = new RouteRequest(grafo, "A", "D");

        // PRIMERA PRUEBA: ADAPTACIÓN POR TIEMPO

        AdaptiveConfigLoader cargador = new AdaptiveConfigLoader();
        AdaptiveConfig configuracion = cargador.cargar();

        MetricsMonitor monitor = new MetricsMonitor();

        AdaptationManager administrador = new AdaptationManager();

        if (configuracion.isCambiarSiSuperaLimite()) {
            administrador.agregarRegla(new ExecutionTimeRule(configuracion));
        }

        if (configuracion.isCambiarSiSuperaErrores()) {
            administrador.agregarRegla(
                    new ErrorThresholdRule(configuracion.getPorcentajeMaximoErrores())
            );
        }

        AdaptiveEngine motor = new AdaptiveEngine(
                selector,
                monitor,
                administrador
        );

        Context contexto = new Context(
                "A",
                "D",
                "tiempo",
                false
        );

        Result<Route> resultado = motor.ejecutar(contexto, solicitud);

        motor.obtenerEjecuciones().forEach(ejecucion -> {

            repositorioEjecuciones.guardar(
                    ejecucion.estrategia(),
                    ejecucion.tiempoMs(),
                    ejecucion.exitoso()
            );

        });

        System.out.println("\nEjecuciones guardadas en PostgreSQL.");

        motor.obtenerHistorial().obtenerTodas().forEach(decision -> {

            repositorioDecisiones.guardar(
                    decision.estrategiaInicial(),
                    decision.estrategiaAlternativa(),
                    decision.motivos()
            );

        });

        System.out.println("\nDecisión adaptativa guardada en PostgreSQL.");

        System.out.println("\nRESULTADO DE LA ADAPTACIÓN POR TIEMPO:");

        if (resultado.exitoso()) {
            System.out.println("Ruta: " + resultado.valor().nodeIds());
            System.out.println("Distancia: " + resultado.valor().totalWeight() + " km");
        } else {
            System.out.println("Error: " + resultado.mensaje());
        }

        System.out.println("\nMÉTRICAS DE LA PRIMERA PRUEBA:");

        monitor.obtenerTodasLasMetricas().forEach((id, metricas) -> {
            System.out.println("\nEstrategia: " + id);
            System.out.println("Ejecuciones: " + metricas.obtenerTotalEjecuciones());
            System.out.println("Errores: " + metricas.obtenerTotalErrores());
            System.out.println("Tiempo promedio: " + metricas.obtenerTiempoPromedioMs() + " ms");
        });

        System.out.println("\nHISTORIAL DE ADAPTACIÓN:");

        motor.obtenerHistorial().obtenerTodas().forEach(decision -> {

            System.out.println("\nEstrategia inicial: " + decision.estrategiaInicial());

            if (decision.huboCambio()) {
                System.out.println("Decisión: cambiar de estrategia");
                System.out.println("Motivos: " + decision.motivos());
                System.out.println("Estrategia alternativa: " + decision.estrategiaAlternativa());
            } else {
                System.out.println("Decisión: mantener estrategia");
            }
        });

        // SEGUNDA PRUEBA: ADAPTACIÓN POR PORCENTAJE DE ERRORES

        Graph grafoSinCoordenadas = crearGrafoSinCoordenadas();

        RouteRequest solicitudAdaptativa = new RouteRequest(
                grafoSinCoordenadas,
                "A",
                "B"
        );

        StrategyRegistry registroDistancia =
                EstrategiasConfiguracion.crearRegistro("distancia");

        StrategySelector selectorDistancia =
                new StrategySelector(registroDistancia);

        MetricsMonitor monitorAStar = new MetricsMonitor();
        AdaptationManager administradorAStar = new AdaptationManager();

        administradorAStar.agregarRegla(new ErrorThresholdRule(30));

        AdaptiveEngine motorAStar = new AdaptiveEngine(
                selectorDistancia,
                monitorAStar,
                administradorAStar
        );

        Context contextoAStar = new Context(
                "A",
                "B",
                "distancia",
                false
        );

        Result<Route> resultadoAStar = motorAStar.ejecutar(
                contextoAStar,
                solicitudAdaptativa
        );

        motorAStar.obtenerEjecuciones().forEach(ejecucion -> {
            repositorioEjecuciones.guardar(
                    ejecucion.estrategia(),
                    ejecucion.tiempoMs(),
                    ejecucion.exitoso()
            );
        });

        System.out.println("\nEjecuciones de adaptación por errores guardadas en PostgreSQL.");

        motorAStar.obtenerHistorial().obtenerTodas().forEach(decision -> {
            repositorioDecisiones.guardar(
                    decision.estrategiaInicial(),
                    decision.estrategiaAlternativa(),
                    decision.motivos()
            );
        });

        System.out.println("\nDecisión de adaptación por errores guardada en PostgreSQL.");

        System.out.println("\n================================");
        System.out.println("       ADAPTACIÓN POR ERRORES");
        System.out.println("================================");

        if (resultadoAStar.exitoso()) {
            System.out.println("Ruta: " + resultadoAStar.valor().nodeIds());
            System.out.println("Distancia: " + resultadoAStar.valor().totalWeight() + " km");
            System.out.println("Mensaje: " + resultadoAStar.mensaje());
        } else {
            System.out.println("Error: " + resultadoAStar.mensaje());
        }

        System.out.println("\nMÉTRICAS DE ADAPTACIÓN:");

        monitorAStar.obtenerTodasLasMetricas().forEach((id, metricas) -> {
            System.out.println("\nEstrategia: " + id);
            System.out.println("Ejecuciones: " + metricas.obtenerTotalEjecuciones());
            System.out.println("Errores: " + metricas.obtenerTotalErrores());
            System.out.println("Tiempo promedio: " + metricas.obtenerTiempoPromedioMs() + " ms");
        });

        System.out.println("\nHISTORIAL DE ADAPTACIÓN:");

        motorAStar.obtenerHistorial().obtenerTodas().forEach(decision -> {
            System.out.println("Estrategia inicial: " + decision.estrategiaInicial());
            System.out.println("Motivos: " + decision.motivos());
            System.out.println("¿Hubo cambio?: " + decision.huboCambio());
            System.out.println("Estrategia alternativa: " + decision.estrategiaAlternativa());
        });
        // TERCERA PRUEBA: SELECCIÓN DE DIJKSTRA SCALA

        StrategyRegistry registroScala =
                EstrategiasConfiguracion.crearRegistro("scala");

        StrategySelector selectorScala =
                new StrategySelector(registroScala);

        MetricsMonitor monitorScala = new MetricsMonitor();
        AdaptationManager administradorScala = new AdaptationManager();

        AdaptiveEngine motorScala = new AdaptiveEngine(
                selectorScala,
                monitorScala,
                administradorScala
        );

        Context contextoScala = new Context(
                "A",
                "D",
                "scala",
                false
        );

        Result<Route> resultadoScala = motorScala.ejecutar(
                contextoScala,
                solicitud
        );

        motorScala.obtenerEjecuciones().forEach(ejecucion -> {
            repositorioEjecuciones.guardar(
                    ejecucion.estrategia(),
                    ejecucion.tiempoMs(),
                    ejecucion.exitoso()
            );
        });

        motorScala.obtenerHistorial().obtenerTodas().forEach(decision -> {
            repositorioDecisiones.guardar(
                    decision.estrategiaInicial(),
                    decision.estrategiaAlternativa(),
                    decision.motivos()
            );
        });

        System.out.println("\n================================");
        System.out.println("       PRUEBA DIJKSTRA SCALA");
        System.out.println("================================");

        if (resultadoScala.exitoso()) {
            System.out.println("Ruta: " + resultadoScala.valor().nodeIds());
            System.out.println("Distancia: " + resultadoScala.valor().totalWeight() + " km");
            System.out.println("Mensaje: " + resultadoScala.mensaje());
        } else {
            System.out.println("Error: " + resultadoScala.mensaje());
        }

        System.out.println("\nMÉTRICAS DE DIJKSTRA SCALA:");

        monitorScala.obtenerTodasLasMetricas().forEach((id, metricas) -> {
            System.out.println("\nEstrategia: " + id);
            System.out.println("Ejecuciones: " + metricas.obtenerTotalEjecuciones());
            System.out.println("Errores: " + metricas.obtenerTotalErrores());
            System.out.println("Tiempo promedio: " + metricas.obtenerTiempoPromedioMs() + " ms");
        });
    }

    private static Graph crearGrafo() {

        Graph grafo = new Graph();

        Node a = new Node("A", "Plaza de Armas", -16.3988, -71.5369);
        Node b = new Node("B", "Yanahuara", -16.3892, -71.5375);
        Node c = new Node("C", "Selva Alegre", -16.3890, -71.5180);
        Node d = new Node("D", "Miraflores", -16.3970, -71.5220);

        grafo.addNode(a);
        grafo.addNode(b);
        grafo.addNode(c);
        grafo.addNode(d);

        grafo.addConnection(new Connection(a, b, 2.0));
        grafo.addConnection(new Connection(a, c, 3.0));
        grafo.addConnection(new Connection(b, d, 3.0));
        grafo.addConnection(new Connection(c, d, 2.0));
        grafo.addConnection(new Connection(b, c, 3.0));

        return grafo;
    }
    private static Graph crearGrafoSinCoordenadas() {

        Graph grafo = new Graph();

        Node a = new Node("A", "Origen");
        Node b = new Node("B", "Destino");

        grafo.addNode(a);
        grafo.addNode(b);

        grafo.addConnection(new Connection(a, b, 5.0));

        return grafo;
    }
}
