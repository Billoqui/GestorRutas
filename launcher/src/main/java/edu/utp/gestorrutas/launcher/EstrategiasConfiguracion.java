package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.adaptive.StrategyRegistry;
import edu.utp.gestorrutas.api.Strategy;
import edu.utp.gestorrutas.plugins.java.astar.AStarStrategy;
import edu.utp.gestorrutas.plugins.java.common.Route;
import edu.utp.gestorrutas.plugins.java.common.RouteRequest;
import edu.utp.gestorrutas.plugins.java.dijkstra.DijkstraStrategy;
import edu.utp.gestorrutas.plugins.scalaplugin.DijkstraScalaStrategy;

import java.util.List;

public class EstrategiasConfiguracion {

    public static StrategyRegistry crearRegistro(String criterio) {

        if (criterio == null || criterio.isBlank()) {
            throw new IllegalArgumentException(
                    "El criterio de selección es obligatorio"
            );
        }

        Strategy<RouteRequest, Route> dijkstra =
                new DijkstraStrategy();

        Strategy<RouteRequest, Route> astar =
                new AStarStrategy();

        Strategy<RouteRequest, Route> scala =
                new DijkstraScalaStrategy();

        List<Strategy<RouteRequest, Route>> estrategias;

        switch (criterio.toLowerCase()) {
            case "tiempo" ->
                    estrategias = List.of(dijkstra, astar, scala);

            case "distancia" ->
                    estrategias = List.of(astar, dijkstra, scala);

            case "scala" ->
                    estrategias = List.of(scala, dijkstra, astar);

            default -> throw new IllegalArgumentException(
                    "Criterio no reconocido: " + criterio
            );
        }

        StrategyRegistry registro = new StrategyRegistry();

        estrategias.forEach(registro::registrar);

        return registro;
    }
}