CREATE TABLE candidato
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    sexo            ENUM('Masculino', 'Feminino', 'Outro') NOT NULL,
    telefone        VARCHAR(15),
    data_nascimento DATE,
    endereco_id     INT,
    FOREIGN KEY (endereco_id) REFERENCES endereco (id)
);