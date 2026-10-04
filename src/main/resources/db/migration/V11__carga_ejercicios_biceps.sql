INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Bíceps con barra recta',                        'DE_PIE',   'BARRA',     'ABIERTA', 'BILATERAL'),
          ('Bíceps con barra Z',                            'DE_PIE',   'BARRA',     'ABIERTA', 'BILATERAL'),
          ('Bíceps con mancuernas alterno',                 'DE_PIE',   'MANCUERNA', 'ABIERTA', 'UNILATERAL'),
          ('Bíceps martillo con mancuernas',                'DE_PIE',   'MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Bíceps en banco Scott / Predicador con barra',  'SENTADO',  'BARRA',     'ABIERTA', 'BILATERAL'),
          ('Bíceps en banco Scott con mancuerna',           'SENTADO',  'MANCUERNA', 'ABIERTA', 'UNILATERAL'),
          ('Bíceps inclinado con mancuernas',               'INCLINADO','MANCUERNA', 'ABIERTA', 'BILATERAL'),
          ('Bíceps concentrado con mancuerna',              'SENTADO',  'MANCUERNA', 'ABIERTA', 'UNILATERAL'),
          ('Bíceps en polea baja',                          'DE_PIE',   'POLEA',     'ABIERTA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Bíceps'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e WHERE e.nombre = v.nombre
);