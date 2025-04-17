CREATE TABLE habilidade
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    candidato_id  INT,
    descricao     VARCHAR(255) NOT NULL,
    nivel         VARCHAR(50),
    especialidade VARCHAR(100),
    FOREIGN KEY (candidato_id) REFERENCES candidato (id)
);