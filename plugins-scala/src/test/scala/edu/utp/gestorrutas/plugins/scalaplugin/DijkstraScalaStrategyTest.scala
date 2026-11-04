package edu.utp.gestorrutas.plugins.scalaplugin

import edu.utp.gestorrutas.domain.{Connection, Graph, Node}
import edu.utp.gestorrutas.plugins.java.common.RouteRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import edu.utp.gestorrutas.plugins.java.dijkstra.DijkstraStrategy
class DijkstraScalaStrategyTest {

  @Test
  def debeEncontrarLaRutaDeMenorPeso(): Unit = {
    val grafo = new Graph()

    val a = new Node("A", "A")
    val b = new Node("B", "B")
    val c = new Node("C", "C")
    val d = new Node("D", "D")

    grafo.addNode(a)
    grafo.addNode(b)
    grafo.addNode(c)
    grafo.addNode(d)

    grafo.addConnection(new Connection(a, b, 4))
    grafo.addConnection(new Connection(b, d, 3))
    grafo.addConnection(new Connection(a, c, 2))
    grafo.addConnection(new Connection(c, d, 8))

    val estrategia = new DijkstraScalaStrategy()
    val solicitud = new RouteRequest(grafo, "A", "D")

    val resultado = estrategia.execute(solicitud)

    assertTrue(resultado.exitoso())
    assertEquals(
      java.util.List.of("A", "B", "D"),
      resultado.valor().nodeIds()
    )
    assertEquals(7.0, resultado.valor().totalWeight())
    val estrategiaJava = new DijkstraStrategy()
    val resultadoJava = estrategiaJava.execute(solicitud)

    assertTrue(resultadoJava.exitoso())

    assertEquals(
      resultadoJava.valor().nodeIds(),
      resultado.valor().nodeIds()
    )

    assertEquals(
      resultadoJava.valor().totalWeight(),
      resultado.valor().totalWeight()
    )
  }
  

  @Test
  def debeInformarCuandoNoExisteUnaRuta(): Unit = {
    val grafo = new Graph()

    val a = new Node("A", "A")
    val b = new Node("B", "B")

    grafo.addNode(a)
    grafo.addNode(b)

    val estrategia = new DijkstraScalaStrategy()
    val solicitud = new RouteRequest(grafo, "A", "B")

    val resultado = estrategia.execute(solicitud)

    assertFalse(resultado.exitoso())
    assertNull(resultado.valor())
    assertEquals(
      "No existe una ruta entre el origen y el destino",
      resultado.mensaje()
    )
  }
}
