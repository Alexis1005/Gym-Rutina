UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Espinales en banco curvo', 'MULTIARTICULAR'),
    ('Espinales c/apoyo',        'MULTIARTICULAR'),
    ('Superman',                 'MULTIARTICULAR'),
    ('Espinales alternos',       'MULTIARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Lumbares'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;