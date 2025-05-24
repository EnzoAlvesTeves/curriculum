CREATE TABLE candidato (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nome                VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    sexo                ENUM('MASCULINO', 'FEMININO', 'OUTRO') NOT NULL,
    telefone            VARCHAR(15),
    data_nascimento     DATE,
    resumo_profissional VARCHAR(1000),
    endereco_id         INT,
    usuario_id          INT,
    FOREIGN KEY (endereco_id) REFERENCES endereco (id),
    FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);