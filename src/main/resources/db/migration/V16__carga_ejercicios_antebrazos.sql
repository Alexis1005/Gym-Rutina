INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Curl de muñeca con pronación c/barra',   'DE_PIE',  'BARRA', 'ABIERTA', 'BILATERAL'),
          ('Curl de muñeca en supinación con barra', 'SENTADO', 'BARRA', 'ABIERTA', 'BILATERAL'),
          ('Flexión radial de muñeca c/rodillo',     'DE_PIE',  'OTRO',  'ABIERTA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Antebrazos'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);