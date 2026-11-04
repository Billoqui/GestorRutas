package edu.utp.gestorrutas.adaptive;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class AdaptiveConfigLoader {

    public AdaptiveConfig cargar() {

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream archivo = getClass()
                .getClassLoader()
                .getResourceAsStream("adaptive-config.json")) {

            if (archivo == null) {
                throw new IllegalStateException(
                        "No se encontró el archivo adaptive-config.json"
                );
            }

            AdaptiveConfigRoot raiz =
                    mapper.readValue(archivo, AdaptiveConfigRoot.class);

            AdaptiveConfig configuracion = raiz.getAdaptacion();

            if (configuracion == null) {
                throw new IllegalArgumentException(
                        "La sección adaptacion es obligatoria"
                );
            }

            if (configuracion.getTiempoMaximoMs() <= 0) {
                throw new IllegalArgumentException(
                        "El tiempo máximo debe ser mayor que cero"
                );
            }

            return configuracion;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo cargar la configuración adaptativa",
                    e
            );
        }
    }
}