-- Inserir os roles
INSERT INTO tb_roles(id, name) VALUES
    ('1', 'ROLE_TRAINEE'),
    ('2', 'ROLE_PERSONAL_TRAINER');

-- Inserir os equipamentos
INSERT INTO tb_equipments (id, name) VALUES
    ('1', 'HALTERES'),
    ('2', 'BARRA FIXA'),
    ('3', 'BICICLETA'),
    ('4', 'SEM EQUIPAMENTO'),
    ('5', 'BARRA COM ANILHAS');

-- Inserir os schemas de Workout (Workouts predefinidos)
INSERT INTO tb_workout_schemas (id, name, description, equipment_id) VALUES
    ('1', 'Flexão de Braço', 'Exercício básico para fortalecimento de peitoral, ombros e tríceps.', '4'),
    ('2', 'Barra Fixa', 'Exercício para desenvolvimento de dorsais e bíceps utilizando uma barra fixa.', '2'),
    ('3', 'Agachamento Livre', 'Exercício para fortalecimento de quadríceps, glúteos e lombar.', '5'),
    ('4', 'Levantamento Terra', 'Movimento composto para fortalecer o corpo inteiro, com foco em pernas, costas e glúteos.', '5'),
    ('5', 'Supino Reto', 'Exercício para desenvolvimento de peitoral, tríceps e ombros.', '1'), 
    ('6', 'Desenvolvimento com Halteres', 'Exercício para ombros utilizando halteres.', '1'), 
    ('7', 'Rosca Direta', 'Exercício isolado para fortalecimento do bíceps.', '1'), 
    ('8', 'Extensão de Tríceps', 'Exercício para desenvolvimento do tríceps, geralmente com halteres ou barras.', '1'), 
    ('9', 'Abdominal', 'Exercício básico para fortalecimento do abdômen.', '4'), 
    ('10', 'Remada Curvada', 'Exercício para fortalecimento das costas, utilizando barra ou halteres.', '1'); 