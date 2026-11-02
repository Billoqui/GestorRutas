package edu.utp.gestorrutas.plugins.java.astar;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Strategy;
import edu.utp.gestorrutas.plugins.java.common.Route;
import edu.utp.gestorrutas.plugins.java.common.RouteRequest;
import edu.utp.gestorrutas.domain.Haversine;
import edu.utp.gestorrutas.domain.Node;
import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.api.Result;
import java.util.*;

public class AStarStrategy implements Strategy<RouteRequest, Route> {

    @Override
    public String id() {
        return "astar";
    }

    @Override
    public boolean supports(Context contexto) {
        return contexto != null
                && !contexto.permitePesosNegativos()
                && contexto.criterio() != null
                && contexto.criterio().equalsIgnoreCase("distancia");
    }

    private double calcularHeuristica(Node actual, Node destino) {
        return Haversine.calcularDistancia(actual, destino);
    }

    private boolean grafoEsCompatible(Graph grafo) {

        if (grafo == null || grafo.getNodes().isEmpty()) {
            return false;
        }

        // Todos los nodos deben tener coordenadas.
        for (Node nodo : grafo.getNodes()) {
            if (!nodo.hasCoordinates()) {
                return false;
            }
        }

        // Los pesos deben ser compatibles con las distancias geográficas.
        for (Connection conexion : grafo.getConnections()) {

            double distanciaReal = Haversine.calcularDistancia(
                    conexion.getOrigen(),
                    conexion.getDestino()
            );

            if (conexion.getPeso() + 1e-9 < distanciaReal) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Result<Route> execute(RouteRequest entrada) {

        long inicio = System.nanoTime();

        if (entrada == null || entrada.graph() == null) {
            return resultadoFallido(
                    "La solicitud y el grafo son obligatorios", inicio
            );
        }

        Graph grafo = entrada.graph();
        String origenId = entrada.origenId();
        String destinoId = entrada.destinoId();

        if (origenId == null || destinoId == null) {
            return resultadoFallido(
                    "El origen y el destino son obligatorios", inicio
            );
        }

        Node origen = grafo.getNode(origenId);
        Node destino = grafo.getNode(destinoId);

        if (origen == null || destino == null) {
            return resultadoFallido(
                    "El origen o el destino no existe en el grafo", inicio
            );
        }

        if (!grafoEsCompatible(grafo)) {
            return resultadoFallido(
                    "El grafo no cumple las condiciones para A*", inicio
            );
        }

        // Costo real acumulado desde el origen.
        Map<String, Double> costos = new HashMap<>();

        // Nodo anterior para reconstruir la ruta.
        Map<String, String> anteriores = new HashMap<>();

        for (Node nodo : grafo.getNodes()) {
            costos.put(nodo.getId(), Double.POSITIVE_INFINITY);
        }

        costos.put(origenId, 0.0);

        // Cola de prioridad según el valor f(n).
        PriorityQueue<NodoPendiente> pendientes = new PriorityQueue<>(
                Comparator.comparingDouble(NodoPendiente::prioridad)
        );

        pendientes.add(new NodoPendiente(
                origenId,
                calcularHeuristica(origen, destino)
        ));

        while (!pendientes.isEmpty()) {

            // Extraemos el nodo con menor prioridad.
            NodoPendiente actual = pendientes.poll();

            Node nodoActual = grafo.getNode(actual.id());

            // Ignoramos entradas antiguas que ya no representan
            // el mejor costo conocido para este nodo.
            double prioridadActual = costos.get(actual.id())
                    + calcularHeuristica(nodoActual, destino);

            if (actual.prioridad() > prioridadActual + 1e-9) {
                continue;
            }

            // Si llegamos al destino, terminamos la búsqueda.
            if (actual.id().equals(destinoId)) {
                break;
            }

            // Revisamos todas las conexiones que salen del nodo actual.
            for (Connection conexion : grafo.getOutgoingConnections(actual.id())) {

                String vecinoId = conexion.getDestino().getId();

                // Calculamos el nuevo costo acumulado.
                double nuevoCosto = costos.get(actual.id())
                        + conexion.getPeso();

                // Solo actualizamos si encontramos un camino mejor.
                if (nuevoCosto < costos.get(vecinoId)) {

                    costos.put(vecinoId, nuevoCosto);
                    anteriores.put(vecinoId, actual.id());

                    double heuristica = calcularHeuristica(
                            conexion.getDestino(),
                            destino
                    );

                    double nuevaPrioridad = nuevoCosto + heuristica;

                    pendientes.add(new NodoPendiente(
                            vecinoId,
                            nuevaPrioridad
                    ));
                }
            }
        }

        // Comprobamos si existe una ruta.
        if (costos.get(destinoId) == Double.POSITIVE_INFINITY) {
            return resultadoFallido(
                    "No existe una ruta entre el origen y el destino",
                    inicio
            );
        }

// Reconstruimos la ruta desde el destino hasta el origen.
        List<String> ruta = new ArrayList<>();

        String nodoActual = destinoId;

        while (nodoActual != null) {
            ruta.add(nodoActual);
            nodoActual = anteriores.get(nodoActual);
        }

// Invertimos la lista para obtener el orden correcto.
        Collections.reverse(ruta);

// Creamos el resultado.
        Route rutaEncontrada = new Route(
                ruta,
                costos.get(destinoId)
        );

        return new Result<>(
                true,
                rutaEncontrada,
                "Ruta encontrada mediante A*",
                (System.nanoTime() - inicio) / 1_000_000
        );
    }

    private Result<Route> resultadoFallido(String mensaje, long inicio) {
        return new Result<>(
                false,
                null,
                mensaje,
                (System.nanoTime() - inicio) / 1_000_000
        );
    }

    private record NodoPendiente(String id, double prioridad) {
    }
}