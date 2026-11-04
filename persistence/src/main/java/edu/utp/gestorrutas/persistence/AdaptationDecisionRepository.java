package edu.utp.gestorrutas.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdaptationDecisionRepository {

    private final DatabaseConnection database;

    public AdaptationDecisionRepository(DatabaseConnection database) {

        if (database == null) {
            throw new IllegalArgumentException(
                    "La conexión a la base de datos no puede ser nula"
            );
        }

        this.database = database;
    }

    public void guardar(
            String estrategiaInicial,
            String estrategiaAlternativa,
            List<String> motivos
    ) {

        if (estrategiaInicial == null || estrategiaInicial.isBlank()
                || motivos == null) {
            throw new IllegalArgumentException(
                    "Los datos de la decisión son obligatorios"
            );
        }

        String motivo = motivos.isEmpty()
                ? "No se requiere adaptación"
                : String.join("; ", motivos);

        boolean huboCambio = estrategiaAlternativa != null;

        String sql = """
                INSERT INTO decisiones_adaptativas (
                    estrategia_inicial,
                    estrategia_alternativa,
                    motivo,
                    hubo_cambio
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = database.conectar();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, estrategiaInicial);
            sentencia.setString(2, estrategiaAlternativa);
            sentencia.setString(3, motivo);
            sentencia.setBoolean(4, huboCambio);

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo guardar la decisión adaptativa",
                    e
            );
        }
    }
}