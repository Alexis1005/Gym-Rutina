INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Encogimientos abdominales',                  'DECUBITO_DORSAL',  'PESO_CORPORAL', 'ABIERTA', 'BILATERAL'),
          ('Crunches con polea alta',                    'OTRO',             'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Elevación de piernas banco declinado',       'DECUBITO_DORSAL',  'PESO_CORPORAL', 'ABIERTA', 'BILATERAL'),
          ('Elevación de rodillas en paralelas',         'DE_PIE',           'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Plancha abdominal',                          'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Plancha lateral',                            'DECUBITO_LATERAL', 'PESO_CORPORAL', 'CERRADA', 'UNILATERAL'),
          ('Rueda abdominal',                            'CUADRUPEDIA',      'OTRO',          'CERRADA', 'BILATERAL'),
          ('Giros rusos',                                'SENTADO',          'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Rodillas al pecho',                          'DECUBITO_DORSAL',  'PESO_CORPORAL', 'ABIERTA', 'BILATERAL'),
          ('Bicho muerto',                               'DECUBITO_DORSAL',  'PESO_CORPORAL', 'ABIERTA', 'BILATERAL'),
          ('Plancha con rotación',                       'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Abdomen'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);