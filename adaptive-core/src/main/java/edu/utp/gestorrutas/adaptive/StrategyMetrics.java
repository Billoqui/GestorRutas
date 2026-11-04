
package edu.utp.gestorrutas.adaptive;

import edu.utp.gestorrutas.api.Result;

public class StrategyMetrics {

    private int totalEjecuciones;
    private int totalErrores;
    private long tiempoTotalMs;

    public void registrarEjecucion(Result<?> resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "El resultado no puede ser nulo"
            );
        }

        totalEjecuciones++;

        if (!resultado.exitoso()) {
            totalErrores++;
        }

        tiempoTotalMs += resultado.tiempoEjecucionMs();
    }

    public int obtenerTotalEjecuciones() {
        return totalEjecuciones;
    }

    public int obtenerTotalErrores() {
        return totalErrores;
    }

    public long obtenerTiempoTotalMs() {
        return tiempoTotalMs;
    }

    public double obtenerTiempoPromedioMs() {

        if (totalEjecuciones == 0) {
            return 0;
        }

        return (double) tiempoTotalMs / totalEjecuciones;
    }
}
