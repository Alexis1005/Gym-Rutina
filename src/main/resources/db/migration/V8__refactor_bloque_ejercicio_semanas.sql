ALTER TABLE bloque_ejercicio
    DROP COLUMN repeticiones,
    DROP COLUMN peso_kg;

-- 2. Agregar cantidad_semanas a rutina
ALTER TABLE rutina
    ADD COLUMN cantidad_semanas INTEGER NOT NULL DEFAULT 1;

-- 3. Crear tabla rutina_bloque_ejercicio_semana
CREATE TABLE rutina_bloque_ejercicio_semana
(
    id                  BIGSERIAL PRIMARY KEY,
    rutina_bloque_id    BIGINT  NOT NULL REFERENCES rutina_bloque (id),
    bloque_ejercicio_id BIGINT  NOT NULL REFERENCES bloque_ejercicio (id),
    numero_semana       INTEGER NOT NULL,
    repeticiones        VARCHAR(255),
    peso_kg             VARCHAR(255)
);

