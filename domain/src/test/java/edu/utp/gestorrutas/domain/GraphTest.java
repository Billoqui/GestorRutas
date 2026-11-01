package edu.utp.gestorrutas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void debeAgregarUnNodo() {
        Graph graph = new Graph();
        Node node = new Node("A", "Plaza");

        graph.addNode(node);

        assertEquals(node, graph.getNode("A"));
    }

    @Test
    void noDebePermitirNodosDuplicados() {
        Graph graph = new Graph();

        graph.addNode(new Node("A", "Plaza"));

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addNode(new Node("A", "Terminal"))
        );
    }

    @Test
    void debeAgregarUnaConexion() {
        Graph graph = new Graph();

        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        graph.addNode(a);
        graph.addNode(b);

        graph.addConnection(new Connection(a, b, 5.2));

        assertEquals(1, graph.getConnections().size());
    }

    @Test
    void noDebePermitirConexionesConNodosInexistentes() {
        Graph graph = new Graph();

        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        graph.addNode(a);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addConnection(new Connection(a, b, 5.2))
        );
    }

    @Test
    void debeObtenerConexionesSalientes() {
        Graph graph = new Graph();

        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");
        Node c = new Node("C", "Universidad");

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);

        graph.addConnection(new Connection(a, b, 5.2));
        graph.addConnection(new Connection(a, c, 3.0));

        assertEquals(2, graph.getOutgoingConnections("A").size());
        assertEquals(0, graph.getOutgoingConnections("B").size());
    }
    @Test
    void noDebePermitirConexionesConObjetosDeNodosNoRegistrados() {
        Graph graph = new Graph();

        Node aRegistrado = new Node("A", "Plaza");
        Node bRegistrado = new Node("B", "Terminal");

        graph.addNode(aRegistrado);
        graph.addNode(bRegistrado);

        // Otro objeto con el mismo ID, pero no registrado en el grafo.
        Node aDiferente = new Node("A", "Otra Plaza");

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addConnection(
                        new Connection(aDiferente, bRegistrado, 5.2)
                )
        );
    }
    @Test
    void noDebePermitirModificarLaColeccionDeNodos() {
        Graph graph = new Graph();
        Node a = new Node("A", "Plaza");

        graph.addNode(a);

        assertThrows(
                UnsupportedOperationException.class,
                () -> graph.getNodes().clear()
        );
    }

    @Test
    void noDebePermitirModificarLaColeccionDeConexiones() {
        Graph graph = new Graph();

        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        graph.addNode(a);
        graph.addNode(b);
        graph.addConnection(new Connection(a, b, 5.2));

        assertThrows(
                UnsupportedOperationException.class,
                () -> graph.getConnections().clear()
        );
    }
    @Test
    void lasConexionesDebenSerDirigidas() {
        Graph graph = new Graph();

        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        graph.addNode(a);
        graph.addNode(b);

        graph.addConnection(new Connection(a, b, 5.2));

        assertEquals(1, graph.getOutgoingConnections("A").size());
        assertEquals(0, graph.getOutgoingConnections("B").size());
    }
}
