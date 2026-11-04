
package edu.utp.gestorrutas.adaptive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdaptationManager {

    private final List<AdaptationRule> reglas = new ArrayList<>();

    public void agregarRegla(AdaptationRule regla) {

        if (regla == null) {
            throw new IllegalArgumentException(
                    "La regla no puede ser nula"
            );
        }

        reglas.add(regla);
    }

    public boolean debeCambiar(StrategyMetrics metricas) {

        for (AdaptationRule regla : reglas) {
            if (regla.debeCambiar(metricas)) {
                return true;
            }
        }

        return false;
    }

    public List<String> obtenerMotivos(StrategyMetrics metricas) {

        List<String> motivos = new ArrayList<>();

        for (AdaptationRule regla : reglas) {
            if (regla.debeCambiar(metricas)) {
                motivos.add(regla.obtenerMotivo());
            }
        }

        return Collections.unmodifiableList(motivos);
    }
}
