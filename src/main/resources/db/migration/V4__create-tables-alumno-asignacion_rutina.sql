CREATE TABLE alumno
(
    id     BIGSERIAL PRIMARY KEY,
    nombre_apellido VARCHAR(50) NOT NULL,
    observaciones   TEXT
);

CREATE TABLE asignacion_rutina
(
    id                BIGSERIAL PRIMARY KEY,
    fecha_asignacion  DATE NOT NULL,
    alumno_id         BIGINT NOT NULL,
    rutina_id         BIGINT NOT NULL,

    CONSTRAINT fk_asignacionrutina_alumno FOREIGN KEY (alumno_id) references alumno (id),
    CONSTRAINT fk_asignacionrutina_rutina FOREIGN KEY (rutina_id) references rutina (id)
);

