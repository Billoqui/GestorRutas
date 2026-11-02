package edu.utp.gestorrutas.plugins.java.astar;

import edu.utp.gestorrutas.domain.Graph;
import edu.utp.gestorrutas.domain.Node;
import edu.utp.gestorrutas.domain.Connection;
import edu.utp.gestorrutas.plugins.java.common.Route;
import edu.utp.gestorrutas.plugins.java.common.RouteRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AStarStrategyTest {

    @Test
    void debeEncontrarLaRutaDeMenorCosto() {

        Graph grafo = new Graph();

        Node a = new Node("A", "Origen", 0.0, 0.0);
        Node b = new Node("B", "Punto B", 0.0, 0.01);
        Node c = new Node("C", "Punto C", 0.0, 0.02);
        Node d = new Node("D", "Destino", 0.0, 0.03);

        grafo.addNode(a);
        grafo.addNode(b);
        grafo.addNode(c);
        grafo.addNode(d);

        grafo.addConnection(new Connection(a, b, 2.0));
        grafo.addConnection(new Connection(b, d, 5.0));
        grafo.addConnection(new Connection(a, c, 3.0));
        grafo.addConnection(new Connection(c, d, 3.0));

        AStarStrategy estrategia = new AStarStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "D");

        var resultado = estrategia.execute(solicitud);

        assertTrue(resultado.exitoso());
        assertEquals(
                java.util.List.of("A", "C", "D"),
                resultado.valor().nodeIds()
        );
        assertEquals(6.0, resultado.valor().totalWeight(), 0.0001);
    }

    @Test
    void debeInformarCuandoNoExisteUnaRuta() {

        Graph grafo = new Graph();

        grafo.addNode(new Node("A", "Origen", 0.0, 0.0));
        grafo.addNode(new Node("B", "Destino", 0.0, 0.01));

        AStarStrategy estrategia = new AStarStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "B");

        var resultado = estrategia.execute(solicitud);

        assertFalse(resultado.exitoso());
        assertNull(resultado.valor());
        assertEquals(
                "No existe una ruta entre el origen y el destino",
                resultado.mensaje()
        );
    }

    @Test
    void noDebeAceptarNodosSinCoordenadas() {

        Graph grafo = new Graph();

        grafo.addNode(new Node("A", "Origen"));
        grafo.addNode(new Node("B", "Destino"));

        AStarStrategy estrategia = new AStarStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "B");

        var resultado = estrategia.execute(solicitud);

        assertFalse(resultado.exitoso());
        assertNull(resultado.valor());
        assertEquals(
                "El grafo no cumple las condiciones para A*",
                resultado.mensaje()
        );
    }

    @Test
    void noDebeAceptarPesosMenoresQueLaDistanciaGeografica() {

        Graph grafo = new Graph();

        Node a = new Node("A", "Origen", 0.0, 0.0);
        Node b = new Node("B", "Destino", 0.0, 0.01);

        grafo.addNode(a);
        grafo.addNode(b);

        // La distancia geográfica es aproximadamente 1.11 km.
        // El peso de 0.5 km es insuficiente.
        grafo.addConnection(new Connection(a, b, 0.5));

        AStarStrategy estrategia = new AStarStrategy();

        RouteRequest solicitud = new RouteRequest(grafo, "A", "B");

        var resultado = estrategia.execute(solicitud);

        assertFalse(resultado.exitoso());
        assertNull(resultado.valor());
        assertEquals(
                "El grafo no cumple las condiciones para A*",
                resultado.mensaje()
        );
    }

    @Test
    void debeAceptarElCriterioDistanciaSinPesosNegativos() {

        AStarStrategy estrategia = new AStarStrategy();

        var contexto = new edu.utp.gestorrutas.api.Context(
                "A", "B", "distancia", false
        );

        assertTrue(estrategia.supports(contexto));
    }

    @Test
    void noDebeAceptarElCriterioTiempo() {

        AStarStrategy estrategia = new AStarStrategy();

        var contexto = new edu.utp.gestorrutas.api.Context(
                "A", "B", "tiempo", false
        );

        assertFalse(estrategia.supports(contexto));
    }
}