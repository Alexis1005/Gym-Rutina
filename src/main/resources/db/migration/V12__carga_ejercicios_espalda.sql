INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Dominadas',                              'DE_PIE',      'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Jalón amplio al pecho en polea',         'SENTADO',     'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón al pecho con agarre neutro',       'SENTADO',     'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón al pecho con agarre en V',         'SENTADO',     'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Jalón unilateral en polea',              'SENTADO',     'POLEA',         'ABIERTA', 'UNILATERAL'),
          ('Remo con barra',                         'INCLINADO',   'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Remo a un brazo',                        'CUADRUPEDIA', 'MANCUERNA',     'ABIERTA', 'UNILATERAL'),
          ('Remo con mancuernas simultáneo',         'INCLINADO',   'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Remo sentado en polea agarre en V',      'SENTADO',     'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Remo sentado en polea a un brazo',       'SENTADO',     'POLEA',         'ABIERTA', 'UNILATERAL'),
          ('Remo sentado en polea agarre amplio',    'SENTADO',     'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Remo en banco de apoyo agarre en V',     'INCLINADO',   'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Remo en banco de apoyo agarre amplio',   'INCLINADO',   'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Pull-over con polea alta',               'DE_PIE',      'POLEA',         'ABIERTA', 'BILATERAL'),
          ('Encogimiento de hombros con mancuerna',  'DE_PIE',      'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Encogimiento de hombros',                'SENTADO',     'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Hiperextensiones en banco',              'INCLINADO',   'PESO_CORPORAL', 'CERRADA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Espalda'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);