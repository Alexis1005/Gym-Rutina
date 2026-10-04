UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Dominadas',                              'MULTIARTICULAR'),
    ('Jalón amplio al pecho en polea',         'MULTIARTICULAR'),
    ('Jalón al pecho con agarre neutro',       'MULTIARTICULAR'),
    ('Jalón al pecho con agarre en V',         'MULTIARTICULAR'),
    ('Jalón unilateral en polea',              'MULTIARTICULAR'),
    ('Remo con barra',                         'MULTIARTICULAR'),
    ('Remo a un brazo',                        'MULTIARTICULAR'),
    ('Remo con mancuernas simultáneo',         'MULTIARTICULAR'),
    ('Remo sentado en polea agarre en V',      'MULTIARTICULAR'),
    ('Remo sentado en polea a un brazo',       'MULTIARTICULAR'),
    ('Remo sentado en polea agarre amplio',    'MULTIARTICULAR'),
    ('Remo en banco de apoyo agarre en V',     'MULTIARTICULAR'),
    ('Remo en banco de apoyo agarre amplio',   'MULTIARTICULAR'),
    ('Pull-over con polea alta',               'MONOARTICULAR'),
    ('Encogimiento de hombros con mancuerna',  'MONOARTICULAR'),
    ('Encogimiento de hombros',                'MONOARTICULAR'),
    ('Hiperextensiones en banco',              'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Espalda'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;
