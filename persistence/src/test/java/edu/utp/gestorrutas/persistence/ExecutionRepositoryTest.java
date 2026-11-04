package edu.utp.gestorrutas.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void debeRealizarOperacionesCRUDEnPostgreSQL() throws SQLException {

        DatabaseConnection database = new DatabaseConnection();
        ExecutionRepository repository =
                new ExecutionRepository(database);

        String estrategiaInicial = "crud-" + UUID.randomUUID();
        String estrategiaActualizada = estrategiaInicial + "-actualizada";

        int id = 0;

        try {
            // 1. CREAR
            repository.guardar(estrategiaInicial, 15, true);

            // Obtener el ID del registro creado
            String sql = """
                    SELECT id
                    FROM ejecuciones
                    WHERE estrategia = ?
                    """;

            try (Connection conexion = database.conectar();
                 PreparedStatement sentencia =
                         conexion.prepareStatement(sql)) {

                sentencia.setString(1, estrategiaInicial);

                try (ResultSet resultado = sentencia.executeQuery()) {

                    assertTrue(resultado.next());
                    id = resultado.getInt("id");
                }
            }

            // 2. VER
            List<String> registros = repository.listar();

            assertTrue(registros.stream().anyMatch(
                    registro -> registro.contains(estrategiaInicial)
            ));

            // 3. ACTUALIZAR
            boolean actualizado = repository.actualizar(
                    id,
                    estrategiaActualizada,
                    99,
                    false
            );

            assertTrue(actualizado);

            // 4. COMPROBAR LA ACTUALIZACION
            registros = repository.listar();

            assertTrue(registros.stream().anyMatch(
                    registro -> registro.contains(estrategiaActualizada)
                            && registro.contains("99 ms")
                            && registro.contains("Exitoso: false")
            ));

            // 5. ELIMINAR
            boolean eliminado = repository.eliminar(id);

            assertTrue(eliminado);

            // 6. COMPROBAR LA ELIMINACION
            registros = repository.listar();

            assertFalse(registros.stream().anyMatch(
                    registro -> registro.contains(estrategiaActualizada)
            ));

        } finally {
            // Limpiar el registro si la prueba falla
            if (id > 0) {
                repository.eliminar(id);
            }
        }
    }
}
