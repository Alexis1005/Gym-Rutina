INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Peso muerto rumano con barra',                       'DE_PIE',           'BARRA',         'CERRADA', 'BILATERAL'),
          ('Peso muerto rumano con mancuernas',                  'DE_PIE',           'MANCUERNA',     'CERRADA', 'BILATERAL'),
          ('Peso muerto rumano unilateral a una pierna',       'DE_PIE',           'MANCUERNA',     'CERRADA', 'UNILATERAL'),
          ('Camilla para femoral',                               'DECUBITO_VENTRAL', 'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Sillón femoral',                                     'SENTADO',          'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Femoral en polea',                                   'DE_PIE',           'POLEA',         'ABIERTA', 'UNILATERAL'),
          ('Curl nórdico',                                       'CUADRUPEDIA',      'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Buenos días c/barra',                                'DE_PIE',           'BARRA',         'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Isquiotibiales'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);