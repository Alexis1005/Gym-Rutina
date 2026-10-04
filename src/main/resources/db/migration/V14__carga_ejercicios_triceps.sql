INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Press francés con barra Z',                    'DECUBITO_DORSAL',  'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Press francés con mancuernas',                 'DECUBITO_DORSAL',  'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Extensiones en polea alta con cuerda',         'DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Extensiones en polea alta con barra recta / V','DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Extensiones unilaterales en polea alta',       'DE_PIE',           'POLEA',         'ABIERTA', 'UNILATERAL'),
          ('Extensiones posteriores',                      'SENTADO',          'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Extensiones posteriores unilateral',           'SENTADO',          'MANCUERNA',     'ABIERTA', 'UNILATERAL'),
          ('Extensiones tras nuca en polea',               'DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón para triceps c/soga',                    'DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón para triceps c/barra recta',             'DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón para triceps unilateral',                'DE_PIE',           'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Patada de tríceps con mancuerna',              'INCLINADO',        'MANCUERNA',     'ABIERTA', 'UNILATERAL'),
          ('Fondos en paralelas',                          'DE_PIE',           'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Fondos en cajón',                              'OTRO',             'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Press de banca agarre cerrado',                'DECUBITO_DORSAL',  'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Flexiones diamante',                           'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Tríceps'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);