
package edu.utp.gestorrutas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void debeCrearUnNodoValido() {
        Node node = new Node("A", "Plaza");

        assertEquals("A", node.getId());
        assertEquals("Plaza", node.getName());
    }

    @Test
    void debeEliminarEspaciosInnecesarios() {
        Node node = new Node(" A ", " Plaza ");

        assertEquals("A", node.getId());
        assertEquals("Plaza", node.getName());
    }

    @Test
    void noDebePermitirIdVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Node("", "Plaza")
        );
    }

    @Test
    void noDebePermitirNombreVacio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Node("A", "   ")
        );
    }

    @Test
    void noDebePermitirIdNull() {
        assertThrows(
                NullPointerException.class,
                () -> new Node(null, "Plaza")
        );
    }

    @Test
    void noDebePermitirNombreNull() {
        assertThrows(
                NullPointerException.class,
                () -> new Node("A", null)
        );
    }
}
