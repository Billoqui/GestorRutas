package edu.utp.gestorrutas.plugins.java.dijkstra;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Result;
import edu.utp.gestorrutas.api.Strategy;
import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.domain.Node;

import java.util.*;

public class DijkstraStrategy implements Strategy<RouteRequest, Route> {

    @Override
    public String id() {
        return "dijkstra";
    }

    @Override
    public boolean supports(Context contexto) {
        return contexto != null
                && !contexto.permitePesosNegativos()
                && contexto.criterio() != null
                && !contexto.criterio().isBlank();
    }

    @Override
    public Result<Route> execute(RouteRequest entrada) {
        long inicio = System.nanoTime();

        if (entrada == null || entrada.graph() == null) {
            return resultadoFallido("La solicitud y el grafo son obligatorios", inicio);
        }

        Graph grafo = entrada.graph();
        String origenId = entrada.origenId();
        String destinoId = entrada.destinoId();

        if (origenId == null || destinoId == null) {
            return resultadoFallido("El origen y el destino son obligatorios", inicio);
        }

        if (grafo.getNode(origenId) == null || grafo.getNode(destinoId) == null) {
            return resultadoFallido("El origen o el destino no existe en el grafo", inicio);
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();

        for (Node nodo : grafo.getNodes()) {
            distancias.put(nodo.getId(), Double.POSITIVE_INFINITY);
        }

        distancias.put(origenId, 0.0);

        PriorityQueue<NodoPendiente> pendientes = new PriorityQueue<>(
                Comparator.comparingDouble(NodoPendiente::distancia)
        );

        pendientes.add(new NodoPendiente(origenId, 0.0));

        while (!pendientes.isEmpty()) {
            NodoPendiente actual = pendientes.poll();

            if (actual.distancia() > distancias.get(actual.id())) {
                continue;
            }

            if (actual.id().equals(destinoId)) {
                break;
            }

            for (Connection conexion : grafo.getOutgoingConnections(actual.id())) {
                String vecinoId = conexion.getDestino().getId();
                double nuevaDistancia = actual.distancia() + conexion.getPeso();

                if (nuevaDistancia < distancias.get(vecinoId)) {
                    distancias.put(vecinoId, nuevaDistancia);
                    anteriores.put(vecinoId, actual.id());

                    pendientes.add(new NodoPendiente(vecinoId, nuevaDistancia));
                }
            }
        }

        if (distancias.get(destinoId) == Double.POSITIVE_INFINITY) {
            return resultadoFallido("No existe una ruta entre el origen y el destino", inicio);
        }

        List<String> ruta = new ArrayList<>();
        String nodoActual = destinoId;

        while (nodoActual != null) {
            ruta.add(nodoActual);
            nodoActual = anteriores.get(nodoActual);
        }

        Collections.reverse(ruta);

        Route rutaEncontrada = new Route(ruta, distancias.get(destinoId));

        return new Result<>(
                true,
                rutaEncontrada,
                "Ruta encontrada",
                tiempoTranscurrido(inicio)
        );
    }

    private Result<Route> resultadoFallido(String mensaje, long inicio) {
        return new Result<>(
                false,
                null,
                mensaje,
                tiempoTranscurrido(inicio)
        );
    }

    private long tiempoTranscurrido(long inicio) {
        return (System.nanoTime() - inicio) / 1_000_000;
    }

    private record NodoPendiente(String id, double distancia) {
    }
}