-- Esquema de la base de datos GestorRutas

-- Tabla para registrar las ejecuciones de las estrategias
CREATE TABLE IF NOT EXISTS ejecuciones (
    id SERIAL PRIMARY KEY,
    estrategia VARCHAR(100) NOT NULL CHECK (TRIM(estrategia) <> ''),
    tiempo_ms BIGINT NOT NULL CHECK (tiempo_ms >= 0),
    exitoso BOOLEAN NOT NULL
);

-- Tabla para registrar las decisiones de adaptación
CREATE TABLE IF NOT EXISTS decisiones_adaptativas (
    id SERIAL PRIMARY KEY,
    estrategia_inicial VARCHAR(100) NOT NULL
        CHECK (TRIM(estrategia_inicial) <> ''),
    estrategia_alternativa VARCHAR(100)
        CHECK (
            estrategia_alternativa IS NULL
            OR TRIM(estrategia_alternativa) <> ''
        ),
    motivo TEXT NOT NULL,
    hubo_cambio BOOLEAN NOT NULL
);