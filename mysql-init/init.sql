-- Criação dos bancos de dados
CREATE DATABASE IF NOT EXISTS ms_curriculum;
CREATE DATABASE IF NOT EXISTS ms_usuario;
CREATE DATABASE IF NOT EXISTS ms_vagas;

-- Criar usuários específicos para cada banco
CREATE USER IF NOT EXISTS 'user_curriculum'@'%' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'user_usuario'@'%' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'user_vagas'@'%' IDENTIFIED BY 'admin';

-- Conceder privilégios aos usuários
GRANT ALL PRIVILEGES ON ms_curriculum.* TO 'user_curriculum'@'%';
GRANT ALL PRIVILEGES ON ms_usuario.* TO 'user_usuario'@'%';
GRANT ALL PRIVILEGES ON ms_vagas.* TO 'user_vagas'@'%';
