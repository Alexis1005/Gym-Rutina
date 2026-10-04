INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Hip Thrust con barra',                       'DECUBITO_DORSAL', 'BARRA',          'CERRADA', 'BILATERAL'),
          ('Hip Thrust unilateral con mancuerna',        'DECUBITO_DORSAL', 'MANCUERNA',      'CERRADA', 'UNILATERAL'),
          ('Puente de glúteo',                           'DECUBITO_DORSAL', 'OTRO',           'CERRADA', 'BILATERAL'),
          ('Patada de glúteo en polea',                  'DE_PIE',          'POLEA',          'ABIERTA', 'UNILATERAL'),
          ('Patada de glúteo en cuadripedia',            'CUADRUPEDIA',     'OTRO',           'ABIERTA', 'UNILATERAL'),
          ('Patada de glúteo en polea a pierna recta',   'DE_PIE',          'POLEA',          'ABIERTA', 'UNILATERAL'),
          ('Patada de glúteo en máquina',                'CUADRUPEDIA',     'MAQUINA',        'ABIERTA', 'UNILATERAL'),
          ('Abducción c/banda',                          'DECUBITO_DORSAL', 'BANDA_ELASTICA', 'ABIERTA', 'BILATERAL'),
          ('Abducción de cadera en máquina',             'SENTADO',         'MAQUINA',        'ABIERTA', 'BILATERAL'),
          ('Abducción de cadera en polea baja',          'DE_PIE',          'POLEA',          'ABIERTA', 'UNILATERAL'),
          ('Sentadilla sumo c/mancuerna',                'DE_PIE',          'MANCUERNA',      'CERRADA', 'BILATERAL'),
          ('Peso muerto sumo c/barra',                   'DE_PIE',          'BARRA',          'CERRADA', 'BILATERAL'),
          ('Prensa 45° con pies arriba y abiertos',      'INCLINADO',       'MAQUINA',        'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Glúteos'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);