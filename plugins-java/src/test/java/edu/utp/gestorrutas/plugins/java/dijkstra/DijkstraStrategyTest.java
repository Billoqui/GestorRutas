package edu.utp.gestorrutas.plugins.java.dijkstra;

import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.domain.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraStrategyTest {

    @Test
    void debeEncontrarLaRutaDeMenorPeso() {
        Graph grafo = new Graph();

        Node a = new Node("A", "A");
        Node b = new Node("B", "B");
        Node c = new Node("C", "C");
        Node d = new Node("D", "D");

        grafo.addNode(a);
        grafo.addNode(b);
        grafo.addNode(c);
        grafo.addNode(d);

        grafo.addConnection(new Connection(a, b, 4));
        grafo.addConnection(new Connection(b, d, 3));
        grafo.addConnection(new Connection(a, c, 2));
        grafo.addConnection(new Connection(c, d, 8));

        DijkstraStrategy estrategia = new DijkstraStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "D");

        var resultado = estrategia.execute(solicitud);

        assertTrue(resultado.exitoso());
        assertEquals(
                java.util.List.of("A", "B", "D"),
                resultado.valor().nodeIds()
        );
        assertEquals(7.0, resultado.valor().totalWeight());
    }
    @Test
    void debeInformarCuandoNoExisteUnaRuta() {
        Graph grafo = new Graph();

        Node a = new Node("A", "A");
        Node b = new Node("B", "B");

        grafo.addNode(a);
        grafo.addNode(b);

        DijkstraStrategy estrategia = new DijkstraStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "B");

        var resultado = estrategia.execute(solicitud);

        assertFalse(resultado.exitoso());
        assertNull(resultado.valor());
        assertEquals(
                "No existe una ruta entre el origen y el destino",
                resultado.mensaje()
        );
    }
}