UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Desplantes de pie en máquina',             'MULTIARTICULAR'),
    ('Desplantes en prensa',                     'MULTIARTICULAR'),
    ('Desplantes de pie c/barra',                'MULTIARTICULAR'),
    ('Desplante unilateral de pie en prensa',    'MULTIARTICULAR'),
    ('Desplante unilateral de pie c/mancuerna',  'MULTIARTICULAR'),
    ('Burrito',                                  'MONOARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Pantorrillas'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;