
package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.adaptive.StrategyRegistry;
import edu.utp.gestorrutas.api.Strategy;

import java.util.List;

public class EstrategiasConfiguracion {

    private static final String DIJKSTRA =
            "edu.utp.gestorrutas.plugins.java.dijkstra.DijkstraStrategy";

    private static final String ASTAR =
            "edu.utp.gestorrutas.plugins.java.astar.AStarStrategy";

    private static final String DIJKSTRA_SCALA =
            "edu.utp.gestorrutas.plugins.scalaplugin.DijkstraScalaStrategy";

    public static StrategyRegistry crearRegistro(String criterio) {

        if (criterio == null || criterio.isBlank()) {
            throw new IllegalArgumentException(
                    "El criterio de selección es obligatorio"
            );
        }

        List<String> clases;

        switch (criterio.toLowerCase()) {
            case "tiempo" ->
                    clases = List.of(DIJKSTRA, ASTAR, DIJKSTRA_SCALA);

            case "distancia" ->
                    clases = List.of(ASTAR, DIJKSTRA, DIJKSTRA_SCALA);

            case "scala" ->
                    clases = List.of(DIJKSTRA_SCALA, DIJKSTRA, ASTAR);

            default -> throw new IllegalArgumentException(
                    "Criterio no reconocido: " + criterio
            );
        }

        StrategyRegistry registro = new StrategyRegistry();

        for (String nombreClase : clases) {
            registro.registrar(cargarEstrategia(nombreClase));
        }

        return registro;
    }

    private static Strategy<?, ?> cargarEstrategia(String nombreClase) {

        try {
            Class<?> clase = Class.forName(nombreClase);

            Class<? extends Strategy> claseEstrategia =
                    clase.asSubclass(Strategy.class);

            return claseEstrategia
                    .getDeclaredConstructor()
                    .newInstance();

        } catch (ClassNotFoundException | ClassCastException |
                 NoSuchMethodException | InstantiationException |
                 IllegalAccessException |
                 java.lang.reflect.InvocationTargetException e) {

            throw new IllegalStateException(
                    "No se pudo cargar la estrategia: " + nombreClase,
                    e
            );
        }
    }
}
