package edu.utp.gestorrutas.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AdaptationDecisionRepositoryTest {

    @Test
    void debeGuardarDecisionAdaptativa() throws Exception {

        DatabaseConnection database = new DatabaseConnection();

        AdaptationDecisionRepository repository =
                new AdaptationDecisionRepository(database);

        String estrategiaInicial = "prueba-inicial";
        String estrategiaAlternativa = "prueba-alternativa";

        repository.guardar(
                estrategiaInicial,
                estrategiaAlternativa,
                java.util.List.of("Tiempo de ejecución elevado")
        );

        String sql = """
                SELECT estrategia_inicial,
                       estrategia_alternativa,
                       motivo,
                       hubo_cambio
                FROM decisiones_adaptativas
                WHERE estrategia_inicial = ?
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, estrategiaInicial);

            try (ResultSet resultado = sentencia.executeQuery()) {

                assertEquals(true, resultado.next());
                assertEquals(
                        estrategiaInicial,
                        resultado.getString("estrategia_inicial")
                );
                assertEquals(
                        estrategiaAlternativa,
                        resultado.getString("estrategia_alternativa")
                );
                assertEquals(
                        "Tiempo de ejecución elevado",
                        resultado.getString("motivo")
                );
                assertEquals(
                        true,
                        resultado.getBoolean("hubo_cambio")
                );
            }
        }
    }
}