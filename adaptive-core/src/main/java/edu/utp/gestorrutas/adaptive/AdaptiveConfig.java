package edu.utp.gestorrutas.adaptive;

public class AdaptiveConfig {

    private long tiempoMaximoMs;
    private boolean cambiarSiSuperaLimite;

    private double porcentajeMaximoErrores;
    private boolean cambiarSiSuperaErrores;

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

    public double getPorcentajeMaximoErrores() {
        return porcentajeMaximoErrores;
    }

    public void setPorcentajeMaximoErrores(double porcentajeMaximoErrores) {
        this.porcentajeMaximoErrores = porcentajeMaximoErrores;
    }

    public boolean isCambiarSiSuperaErrores() {
        return cambiarSiSuperaErrores;
    }

    public void setCambiarSiSuperaErrores(boolean cambiarSiSuperaErrores) {
        this.cambiarSiSuperaErrores = cambiarSiSuperaErrores;
    }
}