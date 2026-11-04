
package edu.utp.gestorrutas.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecutionRepository {

    private final DatabaseConnection database;

    public ExecutionRepository(DatabaseConnection database) {

        if (database == null) {
            throw new IllegalArgumentException(
                    "La conexion a la base de datos no puede ser nula"
            );
        }

        this.database = database;
    }

    // CREAR: insertar una ejecucion
    public void guardar(
            String estrategia,
            long tiempoMs,
            boolean exitoso
    ) {

        if (estrategia == null || estrategia.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la estrategia es obligatorio"
            );
        }

        if (tiempoMs < 0) {
            throw new IllegalArgumentException(
                    "El tiempo no puede ser negativo"
            );
        }

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
                    "No se pudo guardar la ejecucion", e
            );
        }
    }

    // VER: consultar todas las ejecuciones
    public List<String> listar() {

        List<String> ejecuciones = new ArrayList<>();

        String sql = """
                SELECT id, estrategia, tiempo_ms, exitoso
                FROM ejecuciones
                ORDER BY id
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                String registro =
                        "ID: " + resultado.getInt("id")
                                + " | Estrategia: " + resultado.getString("estrategia")
                                + " | Tiempo: " + resultado.getLong("tiempo_ms") + " ms"
                                + " | Exitoso: " + resultado.getBoolean("exitoso");

                ejecuciones.add(registro);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudieron consultar las ejecuciones", e
            );
        }

        return ejecuciones;
    }

    // ACTUALIZAR: modificar una ejecucion por su ID
    public boolean actualizar(
            int id,
            String estrategia,
            long tiempoMs,
            boolean exitoso
    ) {

        if (id <= 0 || estrategia == null || estrategia.isBlank()
                || tiempoMs < 0) {
            throw new IllegalArgumentException(
                    "Los datos de actualizacion no son validos"
            );
        }

        String sql = """
                UPDATE ejecuciones
                SET estrategia = ?, tiempo_ms = ?, exitoso = ?
                WHERE id = ?
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, estrategia);
            sentencia.setLong(2, tiempoMs);
            sentencia.setBoolean(3, exitoso);
            sentencia.setInt(4, id);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo actualizar la ejecucion", e
            );
        }
    }

    // ELIMINAR: borrar una ejecucion por su ID
    public boolean eliminar(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser valido"
            );
        }

        String sql = "DELETE FROM ejecuciones WHERE id = ?";

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo eliminar la ejecucion", e
            );
        }
    }
}
