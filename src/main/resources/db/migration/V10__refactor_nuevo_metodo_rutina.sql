-- Eliminar Bloque y su cadena de relaciones
DROP TABLE IF EXISTS rutina_bloque_ejercicio_semana;
DROP TABLE IF EXISTS rutina_bloque;
DROP TABLE IF EXISTS bloque_ejercicio;
DROP TABLE IF EXISTS bloque;

-- Nueva clasificación del ejercicio
ALTER TABLE ejercicio ADD COLUMN tipo_articular VARCHAR(20);
ALTER TABLE ejercicio ADD COLUMN cadena_cinetica VARCHAR(20);
ALTER TABLE ejercicio ADD COLUMN lateralidad VARCHAR(20);
ALTER TABLE ejercicio ADD COLUMN elemento VARCHAR(20);
ALTER TABLE ejercicio ADD COLUMN posicion VARCHAR(20);

-- Cantidad de días de la rutina (fijo desde la creación)
ALTER TABLE rutina ADD COLUMN cantidad_dias INTEGER NOT NULL DEFAULT 1;

-- Enlace fijo entre Rutina y Ejercicio (reemplaza a Bloque)
CREATE TABLE rutina_ejercicio (
                                  id BIGSERIAL PRIMARY KEY,
                                  rutina_id BIGINT NOT NULL REFERENCES rutina(id),
                                  ejercicio_id BIGINT NOT NULL REFERENCES ejercicio(id),
                                  dia INTEGER NOT NULL,
                                  orden INTEGER NOT NULL
);

-- Datos entrenables, variables por semana
CREATE TABLE rutina_ejercicio_semana (
                                         id BIGSERIAL PRIMARY KEY,
                                         rutina_ejercicio_id BIGINT NOT NULL REFERENCES rutina_ejercicio(id),
                                         semana INTEGER NOT NULL,
                                         series INTEGER NOT NULL,
                                         repeticiones VARCHAR(20) NOT NULL,
                                         peso_kg VARCHAR(20),
                                         descanso_minutos VARCHAR(20),
                                         rir INTEGER,
                                         cadencia VARCHAR(20),
                                         metodo VARCHAR(100)
);