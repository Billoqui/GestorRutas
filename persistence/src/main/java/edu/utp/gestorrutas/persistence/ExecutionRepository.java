
package edu.utp.gestorrutas.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecutionRepository {

    private final DatabaseConnection database;

    public ExecutionRepository(DatabaseConnection database) {

        if (database == null) {
            throw new IllegalArgumentException(
                    "La conexión a la base de datos no puede ser nula"
            );
        }

        this.database = database;
    }

    public void guardar(
            String estrategia,
            long tiempoMs,
            boolean exitoso
    ) {

        String sql = """
                INSERT INTO ejecuciones (
                    estrategia,
                    tiempo_ms,
                    exitoso
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, estrategia);
            sentencia.setLong(2, tiempoMs);
            sentencia.setBoolean(3, exitoso);

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo guardar la ejecución",
                    e
            );
        }

        if (estrategia == null || estrategia.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la estrategia es obligatorio"
            );
        }

        if (tiempoMs < 0) {
            throw new IllegalArgumentException(
                    "El tiempo de ejecución no puede ser negativo"
            );
        }
    }
}
