package edu.utp.gestorrutas.adaptive;

import java.util.List;

public record AdaptationDecision(
        String estrategiaInicial,
        List<String> motivos,
        String estrategiaAlternativa
) {

    public AdaptationDecision {
        if (estrategiaInicial == null || motivos == null) {
            throw new IllegalArgumentException(
                    "Los datos de la decisión son obligatorios"
            );
        }

        motivos = List.copyOf(motivos);
    }

    public boolean huboCambio() {
        return estrategiaAlternativa != null;
    }
}