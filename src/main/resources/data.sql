-- Inserir os roles
INSERT INTO tb_roles(id, name) VALUES
    ('1', 'ROLE_TRAINEE'),
    ('2', 'ROLE_PERSONAL_TRAINER');

-- Inserir Trainee
INSERT INTO tb_trainee(id, first_name, last_name, nickname, email, password, birthdate, gender, personal_trainer_id) VALUES
('1', 'John', 'Doe', 'trainee_jondoe', 'jondoe@email.com', '$2a$10$1SMsRksSGGetXr1uv0cynuSNsObcokBy7d7xG/IZspBWT6iD4XBMa', '2002-04-19', 'masculino', null);

-- Inserir Physical Data
INSERT INTO tb_physical_data(id, created_at, height, imc, weight, trainee_id) VALUES
('1', '2025-03-22', 1.73, 21.55, 64.5, '1');

-- Inserir Trainee Role
INSERT INTO trainee_roles(user_id, role_id) VALUES
('1', '1');

-- Inserir os equipamentos
INSERT INTO tb_equipments (id, name) VALUES
    ('1', 'HALTERES'),
    ('2', 'BARRA FIXA'),
    ('3', 'BICICLETA'),
    ('4', 'SEM EQUIPAMENTO'),
    ('5', 'BARRA COM ANILHAS');

-- Inserir os schemas de Workout (Workouts predefinidos)
INSERT INTO tb_workout_templates (id, name, description, tag, equipment_id) VALUES
    ('1', 'Flexão de Braço', 'Exercício básico para fortalecimento de peitoral, ombros e tríceps.', 'Peito', '4'),
    ('2', 'Barra Fixa', 'Exercício para desenvolvimento de dorsais e bíceps utilizando uma barra fixa.', 'Costas', '2'),
    ('3', 'Agachamento Livre', 'Exercício para fortalecimento de quadríceps, glúteos e lombar.', 'Perna', '5'),
    ('4', 'Levantamento Terra', 'Movimento composto para fortalecer o corpo inteiro, com foco em pernas, costas e glúteos.', 'Perna', '5'),
    ('5', 'Supino Reto', 'Exercício para desenvolvimento de peitoral, tríceps e ombros.', 'Peito', '1'),
    ('6', 'Desenvolvimento com Halteres', 'Exercício para ombros utilizando halteres.', 'Ombro', '1'),
    ('7', 'Rosca Direta', 'Exercício isolado para fortalecimento do bíceps.', 'Bíceps', '1'),
    ('8', 'Extensão de Tríceps', 'Exercício para desenvolvimento do tríceps, geralmente com halteres ou barras.', 'Tríceps', '1'),
    ('9', 'Abdominal', 'Exercício básico para fortalecimento do abdômen.', 'Abdomen', '4'),
    ('10', 'Remada Curvada', 'Exercício para fortalecimento das costas, utilizando barra ou halteres.', 'Costas', '1');