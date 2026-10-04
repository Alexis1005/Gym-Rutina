INSERT INTO ejercicio (nombre, grupo_muscular_id, posicion, elemento, cadena_cinetica, lateralidad)
SELECT v.nombre, g.id, v.posicion, v.elemento, v.cadena_cinetica, v.lateralidad
FROM (VALUES
          ('Press de banca plano',                    'DECUBITO_DORSAL',  'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Press plano con mancuernas',              'DECUBITO_DORSAL',  'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Press inclinado con barra',               'INCLINADO',        'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Press inclinado con mancuernas',          'INCLINADO',        'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Press declinado con barra',               'DECLINADO',        'BARRA',         'ABIERTA', 'BILATERAL'),
          ('Aperturas en banco plano',                'DECUBITO_DORSAL',  'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Aperturas inclinadas',                    'INCLINADO',        'MANCUERNA',     'ABIERTA', 'BILATERAL'),
          ('Apertura en polea',                       'DE_PIE',           'POLEA',         'ABIERTA', 'UNILATERAL'),
          ('Pec-deck',                                'SENTADO',          'MAQUINA',       'ABIERTA', 'BILATERAL'),
          ('Flexiones de brazos (Push-ups)',          'DECUBITO_VENTRAL', 'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Flexiones declinadas',                    'DECLINADO',        'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Flexiones inclinadas',                    'INCLINADO',        'PESO_CORPORAL', 'CERRADA', 'BILATERAL'),
          ('Pullover con mancuerna',                  'DECUBITO_DORSAL',  'MANCUERNA',     'ABIERTA', 'BILATERAL')
     ) AS v(nombre, posicion, elemento, cadena_cinetica, lateralidad)
         JOIN grupo_muscular g ON g.nombre = 'Pectoral'
WHERE NOT EXISTS (
    SELECT 1 FROM ejercicio e
    WHERE e.nombre = v.nombre AND e.grupo_muscular_id = g.id
);