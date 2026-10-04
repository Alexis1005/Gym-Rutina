INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Saltos al cajón más salto vertical',         'DE_PIE', 'CAJON',          'CERRADA', 'BILATERAL'),
          ('Saltos al cajón a pies juntos',              'DE_PIE', 'CAJON',          'CERRADA', 'BILATERAL'),
          ('Saltos laterales sobre línea',               'DE_PIE', 'PESO_CORPORAL',  'CERRADA', 'BILATERAL'),
          ('Skipping rápido en el lugar c/banda',        'DE_PIE', 'BANDA_ELASTICA', 'CERRADA', 'BILATERAL'),
          ('Zancadas con salto y cambio dirección',      'DE_PIE', 'PESO_CORPORAL',  'CERRADA', 'UNILATERAL'),
          ('Drop Jump desde cajón bajo',                 'DE_PIE', 'CAJON',          'CERRADA', 'BILATERAL'),
          ('Desplazamientos laterales explosivos',       'DE_PIE', 'PESO_CORPORAL',  'CERRADA', 'UNILATERAL'),
          ('Saltos verticales con extensión máxima',     'DE_PIE', 'PESO_CORPORAL',  'CERRADA', 'BILATERAL'),
          ('Salto a una pierna',                         'DE_PIE', 'PESO_CORPORAL',  'CERRADA', 'UNILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Pliometría'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);