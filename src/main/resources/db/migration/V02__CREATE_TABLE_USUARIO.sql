CREATE TABLE usuario (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL,
    senha   VARCHAR(255) NOT NULL
);

INSERT INTO usuario (nome, email, senha) VALUES ('admin', 'admin@wisecorp.com', 'admin123');