package edu.utp.gestorrutas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineTest {

    @Test
    void debeCalcularLaDistanciaEntreDosNodos() {
        Node arequipa = new Node(
                "A", "Arequipa", -16.4090, -71.5375
        );

        Node destino = new Node(
                "B", "Destino", -16.4000, -71.5300
        );

        double distancia = Haversine.calcularDistancia(arequipa, destino);

        assertTrue(distancia > 0);
        assertTrue(distancia < 2);
    }

    @Test
    void debeDevolverCeroCuandoLosNodosSonIguales() {
        Node nodo = new Node(
                "A", "Arequipa", -16.4090, -71.5375
        );

        assertEquals(0.0, Haversine.calcularDistancia(nodo, nodo));
    }

    @Test
    void noDebeCalcularDistanciaSinCoordenadas() {
        Node a = new Node("A", "Arequipa");
        Node b = new Node("B", "Destino");

        assertThrows(
                IllegalArgumentException.class,
                () -> Haversine.calcularDistancia(a, b)
        );
    }

    @Test
    void noDebeAceptarNodosNull() {
        Node a = new Node("A", "Arequipa");

        assertThrows(
                IllegalArgumentException.class,
                () -> Haversine.calcularDistancia(null, a)
        );
    }
}