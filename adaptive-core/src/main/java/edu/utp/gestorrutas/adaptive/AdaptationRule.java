package edu.utp.gestorrutas.adaptive;

public interface AdaptationRule {

    boolean debeCambiar(StrategyMetrics metricas);

    String obtenerMotivo();
}
