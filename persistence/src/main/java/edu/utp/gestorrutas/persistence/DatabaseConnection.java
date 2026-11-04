
package edu.utp.gestorrutas.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/gestor_rutas";

    private static final String USUARIO = "postgres";

    private static final String CONTRASENA =
            System.getenv("GESTOR_RUTAS_DB_PASSWORD");

    public Connection conectar() throws SQLException {

        if (CONTRASENA == null || CONTRASENA.isBlank()) {
            throw new IllegalStateException(
                    "No se configuró la contraseña de PostgreSQL"
            );
        }

        return DriverManager.getConnection(
                URL,
                USUARIO,
                CONTRASENA
        );
    }
}
