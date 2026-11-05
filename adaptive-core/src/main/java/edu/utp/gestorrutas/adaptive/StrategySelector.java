package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Context;
import edu.utp.gestorrutas.api.Strategy;

import java.util.Set;

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
        Set<String> excluidas = estrategiaExcluida == null
                ? Set.of()
                : Set.of(estrategiaExcluida);

        return seleccionarExcluyendo(contexto, excluidas);
    }

    public Strategy<?, ?> seleccionarExcluyendo(
            Context contexto,
            Set<String> estrategiasExcluidas
    ) {
        if (contexto == null) {
            throw new IllegalArgumentException(
                    "El contexto no puede ser nulo"
            );
        }

        if (estrategiasExcluidas == null) {
            throw new IllegalArgumentException(
                    "La lista de estrategias excluidas no puede ser nula"
            );
        }

        return registro.obtenerTodas()
                .stream()
                .filter(estrategia ->
                        estrategia.supports(contexto)
                )
                .filter(estrategia ->
                        !estrategiasExcluidas.contains(estrategia.id())
                )
                .findFirst()
                .orElse(null);
    }
}
