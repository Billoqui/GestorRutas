package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.domain.Node;
import edu.utp.gestorrutas.adaptive.AdaptationManager;
import edu.utp.gestorrutas.adaptive.AdaptiveEngine;
import edu.utp.gestorrutas.adaptive.ExecutionTimeRule;
import edu.utp.gestorrutas.adaptive.MetricsMonitor;
import edu.utp.gestorrutas.adaptive.StrategySelector;
import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.plugins.java.common.Route;
import edu.utp.gestorrutas.plugins.java.common.RouteRequest;
import edu.utp.gestorrutas.adaptive.StrategyRegistry;

public class GestorRutasApp {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       GESTOR DE RUTAS");
        System.out.println("================================");

        Graph grafo = crearGrafo();

        StrategyRegistry registro = EstrategiasConfiguracion.crearRegistro();

        StrategySelector selector = new StrategySelector(registro);

        MetricsMonitor monitor = new MetricsMonitor();

        AdaptationManager administrador = new AdaptationManager();
        administrador.agregarRegla(new ExecutionTimeRule(0));

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

        RouteRequest solicitud = new RouteRequest(grafo, "A", "D");

        Result<Route> resultado = motor.ejecutar(contexto, solicitud);

        System.out.println("\nResultado de la ruta:");

        if (resultado.exitoso()) {
            System.out.println("Ruta: " + resultado.valor().nodeIds());
            System.out.println("Distancia: " + resultado.valor().totalWeight() + " km");
        } else {
            System.out.println("Error: " + resultado.mensaje());
        }
        System.out.println("\nMétricas de las estrategias:");

        monitor.obtenerTodasLasMetricas().forEach((id, metricas) -> {
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
        grafo.addConnection(new Connection(b, c, 2.0));

        return grafo;
    }
}
