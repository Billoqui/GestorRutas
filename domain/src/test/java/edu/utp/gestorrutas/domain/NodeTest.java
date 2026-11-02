
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

    @Test
    void debeCrearUnNodoConCoordenadas() {
        Node nodo = new Node("A", "Arequipa", -16.4090, -71.5375);

        assertEquals(-16.4090, nodo.getLatitude());
        assertEquals(-71.5375, nodo.getLongitude());
        assertTrue(nodo.hasCoordinates());
    }

    @Test
    void debeIndicarCuandoUnNodoNoTieneCoordenadas() {
        Node nodo = new Node("A", "Arequipa");

        assertFalse(nodo.hasCoordinates());
    }

    @Test
    void noDebePermitirUnaLatitudFueraDeRango() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Node("A", "Arequipa", 95, -71.5375)
        );
    }

    @Test
    void noDebePermitirUnaLongitudFueraDeRango() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Node("A", "Arequipa", -16.4090, 190)
        );
    }
}
