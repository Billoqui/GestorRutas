package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Strategy;

public class StrategySelector {

    private final StrategyRegistry registro;

    public StrategySelector(StrategyRegistry registro) {
        if (registro == null) {
            throw new IllegalArgumentException(
                    "El registro no puede ser nulo"
            );
        }

        this.registro = registro;
    }

    public Strategy<?, ?> seleccionar(Context contexto) {
        return seleccionar(contexto, null);
    }

    public Strategy<?, ?> seleccionar(
            Context contexto,
            String estrategiaExcluida
    ) {
        if (contexto == null) {
            throw new IllegalArgumentException(
                    "El contexto no puede ser nulo"
            );
        }

        return registro.obtenerTodas()
                .stream()
                .filter(estrategia ->
                        estrategia.supports(contexto)
                )
                .filter(estrategia ->
                        estrategiaExcluida == null
                                || !estrategia.id().equals(estrategiaExcluida)
                )
                .findFirst()
                .orElse(null);
    }
}