ALTER TABLE rutina_bloque
DROP COLUMN etiqueta_semana,
    ADD COLUMN orden INTEGER NOT NULL DEFAULT 0;