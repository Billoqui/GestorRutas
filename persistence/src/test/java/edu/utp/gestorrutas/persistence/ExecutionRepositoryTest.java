
package edu.utp.gestorrutas.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutionRepositoryTest {

    @Test
    void debeGuardarUnaEjecucionEnPostgreSQL() throws SQLException {

        DatabaseConnection database = new DatabaseConnection();
        ExecutionRepository repository =
                new ExecutionRepository(database);

        String estrategia = "prueba-jdbc-" + UUID.randomUUID();
        repository.guardar(estrategia, 15, true);

        String sql = """
                SELECT COUNT(*)
                FROM ejecuciones
                WHERE estrategia = ?
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, estrategia);

            try (ResultSet resultado = sentencia.executeQuery()) {

                resultado.next();

                assertEquals(1, resultado.getInt(1));
            }
        }
    }
}
