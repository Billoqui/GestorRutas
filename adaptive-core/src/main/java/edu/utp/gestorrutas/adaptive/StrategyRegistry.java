package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StrategyRegistry {

    private final List<Strategy<?, ?>> estrategias = new ArrayList<>();

    public void registrar(Strategy<?, ?> estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException(
                    "La estrategia no puede ser nula"
            );
        }

        if (buscarPorId(estrategia.id()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una estrategia con ese ID"
            );
        }

        estrategias.add(estrategia);
    }

    public List<Strategy<?, ?>> obtenerTodas() {
        return Collections.unmodifiableList(estrategias);
    }

    public Strategy<?, ?> buscarPorId(String id) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
