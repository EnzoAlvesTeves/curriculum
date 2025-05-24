CREATE TABLE endereco (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    rua         VARCHAR(500) NOT NULL,
    numero      VARCHAR(255),
    complemento VARCHAR(255),
    cidade      VARCHAR(100) NOT NULL,
    estado      CHAR(2)      NOT NULL,
    cep         VARCHAR(10)  NOT NULL,
    bairro      varchar(255)
);