
package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.adaptive.StrategyRegistry;
import edu.utp.gestorrutas.plugins.java.dijkstra.DijkstraStrategy;
import edu.utp.gestorrutas.plugins.java.astar.AStarStrategy;
import edu.utp.gestorrutas.plugins.scalaplugin.DijkstraScalaStrategy;

public class EstrategiasConfiguracion {

    public static StrategyRegistry crearRegistro() {

        StrategyRegistry registro = new StrategyRegistry();

        registro.registrar(new DijkstraStrategy());
        registro.registrar(new AStarStrategy());
        registro.registrar(new DijkstraScalaStrategy());

        return registro;
    }
}
