UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Hip Thrust con barra',                      'MULTIARTICULAR'),
    ('Hip Thrust unilateral con mancuerna',       'MULTIARTICULAR'),
    ('Puente de glúteo',                          'MULTIARTICULAR'),
    ('Patada de glúteo en polea',                 'MONOARTICULAR'),
    ('Patada de glúteo en cuadripedia',           'MONOARTICULAR'),
    ('Patada de glúteo en polea a pierna recta',  'MONOARTICULAR'),
    ('Patada de glúteo en máquina',               'MONOARTICULAR'),
    ('Abducción c/banda',                         'MONOARTICULAR'),
    ('Abducción de cadera en máquina',            'MONOARTICULAR'),
    ('Abducción de cadera en polea baja',         'MONOARTICULAR'),
    ('Sentadilla sumo c/mancuerna',               'MULTIARTICULAR'),
    ('Peso muerto sumo c/barra',                  'MULTIARTICULAR'),
    ('Prensa 45° con pies arriba y abiertos',     'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Glúteos'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;