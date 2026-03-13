CREATE TABLE grupo_muscular
(
    id     BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE ejercicio
(
    id               BIGSERIAL PRIMARY KEY,
    nombre           VARCHAR(255) NOT NULL,
    descripcion      TEXT,
    grupo_muscular_id BIGINT       NOT NULL,

    CONSTRAINT fk_ejercicio_grupomuscular FOREIGN KEY (grupo_muscular_id) references grupo_muscular (id)
);

CREATE TABLE bloque_ejercicio
(
    id              BIGSERIAL PRIMARY KEY,
    series          INTEGER          NOT NULL,
    repeticiones    INTEGER          NOT NULL,
    peso_kg          DOUBLE PRECISION NOT NULL,
    descanso_minutos DOUBLE PRECISION NOT NULL,
    ejercicio_id    BIGINT           NOT NULL,
    bloque_id       BIGINT           NOT NULL,

    CONSTRAINT fk_bloqueejercicio_ejercicio FOREIGN KEY (ejercicio_id) REFERENCES ejercicio (id),
    CONSTRAINT fk_bloqueejercicio_bloque FOREIGN KEY (bloque_id) REFERENCES bloque (id)
);
