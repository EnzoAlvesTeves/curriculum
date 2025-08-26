CREATE TABLE educacao (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    candidato_id INT,
    grau         VARCHAR(50)  NOT NULL,
    data_inicio  DATE,
    data_fim     DATE,
    instituicao  VARCHAR(100) NOT NULL,
    curso        VARCHAR(100) NOT NULL,
    FOREIGN KEY (candidato_id) REFERENCES candidato (id)
);