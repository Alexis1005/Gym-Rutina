INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Espinales en banco curvo', 'INCLINADO',        'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Espinales c/apoyo',        'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Superman',                 'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Espinales alternos',       'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'UNILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Lumbares'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);