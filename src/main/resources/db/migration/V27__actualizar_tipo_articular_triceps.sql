UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Press francés con barra Z',                     'MONOARTICULAR'),
    ('Press francés con mancuernas',                  'MONOARTICULAR'),
    ('Extensiones en polea alta con cuerda',          'MONOARTICULAR'),
    ('Extensiones en polea alta con barra recta / V', 'MONOARTICULAR'),
    ('Extensiones unilaterales en polea alta',        'MONOARTICULAR'),
    ('Extensiones posteriores',                       'MONOARTICULAR'),
    ('Extensiones posteriores unilateral',            'MONOARTICULAR'),
    ('Extensiones tras nuca en polea',                'MONOARTICULAR'),
    ('Jalón para triceps c/soga',                     'MONOARTICULAR'),
    ('Jalón para triceps c/barra recta',              'MONOARTICULAR'),
    ('Jalón para triceps unilateral',                 'MONOARTICULAR'),
    ('Patada de tríceps con mancuerna',               'MONOARTICULAR'),
    ('Fondos en paralelas',                           'MULTIARTICULAR'),
    ('Fondos en cajón',                               'MULTIARTICULAR'),
    ('Press de banca agarre cerrado',                 'MULTIARTICULAR'),
    ('Flexiones diamante',                            'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Tríceps'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;