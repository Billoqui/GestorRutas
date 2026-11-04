package edu.utp.gestorrutas.persistence;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConnectionTest {

    @Test
    void debeConectarseAPostgreSQL() throws SQLException {

        DatabaseConnection database = new DatabaseConnection();

        try (Connection conexion = database.conectar()) {

            assertNotNull(conexion);
            assertTrue(conexion.isValid(5));
        }
    }
}
