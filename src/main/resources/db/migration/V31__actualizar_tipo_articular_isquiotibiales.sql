UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Peso muerto rumano con barra',                'MULTIARTICULAR'),
    ('Peso muerto rumano con mancuernas',           'MULTIARTICULAR'),
    ('Peso muerto rumano unilateral a una pierna',  'MULTIARTICULAR'),
    ('Camilla para femoral',                        'MONOARTICULAR'),
    ('Sillón femoral',                              'MONOARTICULAR'),
    ('Femoral en polea',                            'MONOARTICULAR'),
    ('Curl nórdico',                                'MONOARTICULAR'),
    ('Buenos días c/barra',                         'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Isquiotibiales'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;