CREATE TABLE rutina
(
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE bloque
(
    id     BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE rutina_bloque
(
    id              BIGSERIAL PRIMARY KEY,
    etiqueta_semana VARCHAR(50),
    rutina_id       BIGINT NOT NULL,
    bloque_id       BIGINT NOT NULL,
    CONSTRAINT fk_rutinabloque_rutina FOREIGN KEY (rutina_id) REFERENCES rutina (id),
    CONSTRAINT fk_rutinabloque_bloque FOREIGN KEY (bloque_id) REFERENCES bloque (id)
);