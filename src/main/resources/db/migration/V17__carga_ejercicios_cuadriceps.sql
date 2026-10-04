INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Sentadilla c/barra',                            'DE_PIE',    'BARRA',         'CERRADA', 'BILATERAL'),
          ('Sentadilla frontal c/barra',                    'DE_PIE',    'BARRA',         'CERRADA', 'BILATERAL'),
          ('Sentadilla Goblet con mancuerna',               'DE_PIE',    'MANCUERNA',     'CERRADA', 'BILATERAL'),
          ('Sentadilla Búlgara',                            'DE_PIE',    'MANCUERNA',     'CERRADA', 'UNILATERAL'),
          ('Prensa 45° de piernas',                         'INCLINADO', 'MAQUINA',       'CERRADA', 'BILATERAL'),
          ('Prensa unilateral de piernas',                  'INCLINADO', 'MAQUINA',       'CERRADA', 'UNILATERAL'),
          ('Extensión en camilla p/cuádriceps',             'SENTADO',   'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Extensión unilateral en camilla p/cuádriceps',  'SENTADO',   'MAQUINA',       'ABIERTA', 'UNILATERAL'),
          ('Estocadas caminando',                           'DE_PIE',    'MANCUERNA',     'CERRADA', 'UNILATERAL'),
          ('Estocadas fijas con barra',                     'DE_PIE',    'BARRA',         'CERRADA', 'UNILATERAL'),
          ('Estocadas fijas con mancuernas',                'DE_PIE',    'MANCUERNA',     'CERRADA', 'UNILATERAL'),
          ('Estocadas posteriores',                         'DE_PIE',    'MANCUERNA',     'CERRADA', 'UNILATERAL'),
          ('Step-up en cajón',                              'DE_PIE',    'CAJON',         'CERRADA', 'UNILATERAL'),
          ('Sentadilla en máquina',                         'DE_PIE',    'MAQUINA',       'CERRADA', 'BILATERAL'),
          ('Sentadilla Hack en máquina',                    'INCLINADO', 'MAQUINA',       'CERRADA', 'BILATERAL'),
          ('Aducción',                                      'SENTADO',   'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Sentadilla en isométrica',                      'DE_PIE',    'PESO_CORPORAL', 'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Cuádriceps'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);