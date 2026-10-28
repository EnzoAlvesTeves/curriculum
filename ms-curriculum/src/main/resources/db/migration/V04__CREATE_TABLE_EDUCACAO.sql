CREATE TABLE educacao (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso           VARCHAR(100) NOT NULL,
    grau            VARCHAR(100) NOT NULL,
    instituicao     VARCHAR(100) NOT NULL,
    data_inicio     DATE NOT NULL ,
    data_fim        DATE,
    id_candidato    BIGINT NOT NULL,
    FOREIGN KEY (id_candidato) REFERENCES candidato (id) ON DELETE CASCADE
);