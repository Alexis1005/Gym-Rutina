-- Tipo articular de los ejercicios de Bíceps
UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Bíceps con barra recta',                          'MONOARTICULAR'),
    ('Bíceps con barra Z',                              'MONOARTICULAR'),
    ('Bíceps con mancuernas alterno',                   'MONOARTICULAR'),
    ('Bíceps martillo con mancuernas',                  'MONOARTICULAR'),
    ('Bíceps en banco Scott / Predicador con barra',    'MONOARTICULAR'),
    ('Bíceps en banco Scott con mancuerna',             'MONOARTICULAR'),
    ('Bíceps inclinado con mancuernas',                 'MONOARTICULAR'),
    ('Bíceps concentrado con mancuerna',                'MONOARTICULAR'),
    ('Bíceps en polea baja',                            'MONOARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Bíceps'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;