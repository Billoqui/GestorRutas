package edu.utp.gestorrutas.adaptive;

public class AdaptiveConfig {

    private long tiempoMaximoMs;
    private boolean cambiarSiSuperaLimite;

    public long getTiempoMaximoMs() {
        return tiempoMaximoMs;
    }

    public void setTiempoMaximoMs(long tiempoMaximoMs) {
        this.tiempoMaximoMs = tiempoMaximoMs;
    }

    public boolean isCambiarSiSuperaLimite() {
        return cambiarSiSuperaLimite;
    }

    public void setCambiarSiSuperaLimite(boolean cambiarSiSuperaLimite) {
        this.cambiarSiSuperaLimite = cambiarSiSuperaLimite;
    }
}