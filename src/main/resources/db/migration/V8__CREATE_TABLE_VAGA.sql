CREATE TABLE vaga
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR (255) NOT NULL,
    descricao VARCHAR (255) NOT NULL,
    empresa VARCHAR (255) NOT NULL,
    beneficios VARCHAR (255) NOT NULL,
    salario DECIMAL (10, 2)
);