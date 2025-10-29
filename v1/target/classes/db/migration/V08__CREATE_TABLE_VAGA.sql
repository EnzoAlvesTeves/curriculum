CREATE TABLE vaga (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR (255) NOT NULL,
    descricao   VARCHAR (500) NOT NULL,
    empresa     VARCHAR (255) NOT NULL,
    beneficios  VARCHAR (255),
    salario     DECIMAL (10, 2)
);