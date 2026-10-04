UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Sentadilla c/barra',                            'MULTIARTICULAR'),
    ('Sentadilla frontal c/barra',                    'MULTIARTICULAR'),
    ('Sentadilla Goblet con mancuerna',               'MULTIARTICULAR'),
    ('Sentadilla Búlgara',                            'MULTIARTICULAR'),
    ('Prensa 45° de piernas',                         'MULTIARTICULAR'),
    ('Prensa unilateral de piernas',                  'MULTIARTICULAR'),
    ('Extensión en camilla p/cuádriceps',             'MONOARTICULAR'),
    ('Extensión unilateral en camilla p/cuádriceps',  'MONOARTICULAR'),
    ('Estocadas caminando',                           'MULTIARTICULAR'),
    ('Estocadas fijas con barra',                     'MULTIARTICULAR'),
    ('Estocadas fijas con mancuernas',                'MULTIARTICULAR'),
    ('Estocadas posteriores',                         'MULTIARTICULAR'),
    ('Step-up en cajón',                              'MULTIARTICULAR'),
    ('Sentadilla en máquina',                         'MULTIARTICULAR'),
    ('Sentadilla Hack en máquina',                    'MULTIARTICULAR'),
    ('Aducción',                                      'MONOARTICULAR'),
    ('Sentadilla en isométrica',                      'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Cuádriceps'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;