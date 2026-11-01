
package edu.utp.gestorrutas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {

    @Test
    void debeCrearUnaConexionValida() {
        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        Connection connection = new Connection(a, b, 5.2);

        assertEquals(a, connection.getOrigen());
        assertEquals(b, connection.getDestino());
        assertEquals(5.2, connection.getPeso());
    }

    @Test
    void debePermitirPesoCero() {
        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        Connection connection = new Connection(a, b, 0);

        assertEquals(0, connection.getPeso());
    }

    @Test
    void noDebePermitirOrigenNull() {
        Node b = new Node("B", "Terminal");

        assertThrows(
                NullPointerException.class,
                () -> new Connection(null, b, 5.2)
        );
    }

    @Test
    void noDebePermitirDestinoNull() {
        Node a = new Node("A", "Plaza");

        assertThrows(
                NullPointerException.class,
                () -> new Connection(a, null, 5.2)
        );
    }

    @Test
    void noDebePermitirPesoNegativo() {
        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Connection(a, b, -5)
        );
    }

    @Test
    void noDebePermitirPesoNaN() {
        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Connection(a, b, Double.NaN)
        );
    }

    @Test
    void noDebePermitirPesoInfinito() {
        Node a = new Node("A", "Plaza");
        Node b = new Node("B", "Terminal");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Connection(a, b, Double.POSITIVE_INFINITY)
        );
    }
}
