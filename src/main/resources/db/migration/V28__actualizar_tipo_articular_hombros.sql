UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Press militar con barra',                   'MULTIARTICULAR'),
    ('Press militar sentado con mancuernas',      'MULTIARTICULAR'),
    ('Press Arnold',                              'MULTIARTICULAR'),
    ('Elevaciones laterales con mancuernas',      'MONOARTICULAR'),
    ('Elevaciones laterales en polea a un brazo', 'MONOARTICULAR'),
    ('Elevaciones frontales con mancuernas',      'MONOARTICULAR'),
    ('Elevaciones frontales',                     'MONOARTICULAR'),
    ('Vuelos posteriores con mancuernas',         'MONOARTICULAR'),
    ('Face Pull en polea alta',                   'MULTIARTICULAR'),
    ('Remo al mentón con barra',                  'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Hombros'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;