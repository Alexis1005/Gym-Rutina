INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Press militar con barra',                   'DE_PIE',    'BARRA',     'ABIERTA', 'BILATERAL'),
          ('Press militar sentado con mancuernas',      'SENTADO',   'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Press Arnold',                              'SENTADO',   'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Elevaciones laterales con mancuernas',      'DE_PIE',    'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Elevaciones laterales en polea a un brazo', 'DE_PIE',    'POLEA',     'ABIERTA', 'UNILATERAL'),
          ('Elevaciones frontales con mancuernas',      'DE_PIE',    'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Elevaciones frontales',                     'DE_PIE',    'OTRO',      'ABIERTA', 'BILATERAL'),
          ('Vuelos posteriores con mancuernas',         'INCLINADO', 'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Face Pull en polea alta',                   'DE_PIE',    'POLEA',     'ABIERTA', 'BILATERAL'),
          ('Remo al mentón con barra',                  'DE_PIE',    'BARRA',     'ABIERTA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Hombros'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);