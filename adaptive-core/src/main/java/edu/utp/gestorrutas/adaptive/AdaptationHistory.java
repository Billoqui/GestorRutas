package edu.utp.gestorrutas.adaptive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdaptationHistory {

    private final List<AdaptationDecision> decisiones = new ArrayList<>();

    public void registrar(AdaptationDecision decision) {

        if (decision == null) {
            throw new IllegalArgumentException(
                    "La decisión no puede ser nula"
            );
        }

        decisiones.add(decision);
    }

    public List<AdaptationDecision> obtenerTodas() {
        return Collections.unmodifiableList(decisiones);
    }

    public void limpiar() {
        decisiones.clear();
    }
}
