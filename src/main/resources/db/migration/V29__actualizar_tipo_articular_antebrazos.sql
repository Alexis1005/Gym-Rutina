UPDATE ejercicio e
SET tipo_articular = v.tipo
    FROM (VALUES
    ('Curl de muñeca con pronación c/barra',   'MONOARTICULAR'),
    ('Curl de muñeca en supinación con barra', 'MONOARTICULAR'),
    ('Flexión radial de muñeca c/rodillo',     'MONOARTICULAR')
) AS v(nombre, tipo)
JOIN grupo_muscular g ON g.nombre = 'Antebrazos'
WHERE e.nombre = v.nombre
  AND e.grupo_muscular_id = g.id;