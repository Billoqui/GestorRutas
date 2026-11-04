
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

public class GestorRutasApp {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       GESTOR DE RUTAS");
        System.out.println("================================");

        Graph grafo = crearGrafo();

        StrategyRegistry registro = EstrategiasConfiguracion.crearRegistro();
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

        // SEGUNDA PRUEBA: SELECCIÓN DE A*

        MetricsMonitor monitorAStar = new MetricsMonitor();
        AdaptationManager administradorAStar = new AdaptationManager();

        AdaptiveEngine motorAStar = new AdaptiveEngine(
                selector,
                monitorAStar,
                administradorAStar
        );

        Context contextoAStar = new Context(
                "A",
                "D",
                "distancia",
                false
        );

        Result<Route> resultadoAStar = motorAStar.ejecutar(
                contextoAStar,
                solicitud
        );

        System.out.println("\n================================");
        System.out.println("       PRUEBA ADAPTATIVA A*");
        System.out.println("================================");

        if (resultadoAStar.exitoso()) {
            System.out.println("Ruta: " + resultadoAStar.valor().nodeIds());
            System.out.println("Distancia: " + resultadoAStar.valor().totalWeight() + " km");
            System.out.println("Mensaje: " + resultadoAStar.mensaje());
        } else {
            System.out.println("Error: " + resultadoAStar.mensaje());
        }

        System.out.println("\nMÉTRICAS DE A*:");

        monitorAStar.obtenerTodasLasMetricas().forEach((id, metricas) -> {
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
}
