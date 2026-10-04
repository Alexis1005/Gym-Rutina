INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Desplantes de pie en máquina',               'DE_PIE',          'MAQUINA',   'CERRADA', 'BILATERAL'),
          ('Desplantes en prensa',                       'DECUBITO_DORSAL', 'MAQUINA',   'CERRADA', 'BILATERAL'),
          ('Desplantes de pie c/barra',                  'DE_PIE',          'BARRA',     'CERRADA', 'BILATERAL'),
          ('Desplante unilateral de pie en prensa',      'DE_PIE',          'MAQUINA',   'CERRADA', 'UNILATERAL'),
          ('Desplante unilateral de pie c/mancuerna',    'DE_PIE',          'MANCUERNA', 'CERRADA', 'UNILATERAL'),
          ('Burrito',                                    'SENTADO',         'MAQUINA',   'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Pantorrillas'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);