
package edu.utp.gestorrutas.domain;

import java.util.*;

/**
 * Representa un grafo formado por nodos y conexiones dirigidas.
 */
public class Graph {

    private final Map<String, Node> nodes = new LinkedHashMap<>();
    private final List<Connection> connections = new ArrayList<>();

    /**
     * Agrega un nodo al grafo.
     */
    public void addNode(Node node) {
        Objects.requireNonNull(node, "El nodo no puede ser null");

        if (nodes.containsKey(node.getId())) {
            throw new IllegalArgumentException("Ya existe un nodo con ese ID");
        }

        nodes.put(node.getId(), node);
    }

    /**
     * Agrega una conexión entre dos nodos existentes.
     */
    public void addConnection(Connection connection) {
        Objects.requireNonNull(connection, "La conexión no puede ser null");

        String origenId = connection.getOrigen().getId();
        String destinoId = connection.getDestino().getId();

        if (nodes.get(origenId) != connection.getOrigen()
                || nodes.get(destinoId) != connection.getDestino()) {

            throw new IllegalArgumentException(
                    "Los nodos de la conexión deben ser los registrados en el grafo"
            );
        }

        connections.add(connection);
    }

    /**
     * Busca un nodo por su identificador.
     */
    public Node getNode(String id) {
        return nodes.get(id);
    }

    /**
     * Devuelve todos los nodos del grafo.
     */
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    /**
     * Devuelve todas las conexiones del grafo.
     */
    public List<Connection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Obtiene las conexiones que salen de un nodo.
     */
    public List<Connection> getOutgoingConnections(String nodeId) {
        return connections.stream()
                .filter(connection -> connection.getOrigen().getId().equals(nodeId))
                .toList();
    }
}
