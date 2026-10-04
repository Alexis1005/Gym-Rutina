UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Press de banca plano',               'MULTIARTICULAR'),
    ('Press plano con mancuernas',         'MULTIARTICULAR'),
    ('Press inclinado con barra',          'MULTIARTICULAR'),
    ('Press inclinado con mancuernas',     'MULTIARTICULAR'),
    ('Press declinado con barra',          'MULTIARTICULAR'),
    ('Aperturas en banco plano',           'MONOARTICULAR'),
    ('Aperturas inclinadas',               'MONOARTICULAR'),
    ('Apertura en polea',                  'MONOARTICULAR'),
    ('Pec-deck',                           'MONOARTICULAR'),
    ('Flexiones de brazos (Push-ups)',     'MULTIARTICULAR'),
    ('Flexiones declinadas',               'MULTIARTICULAR'),
    ('Flexiones inclinadas',               'MULTIARTICULAR'),
    ('Pullover con mancuerna',             'MONOARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Pectoral'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;