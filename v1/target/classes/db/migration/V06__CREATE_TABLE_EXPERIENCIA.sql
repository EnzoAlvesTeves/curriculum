CREATE TABLE experiencia (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    candidato_id INT,
    cargo        VARCHAR(100) NOT NULL,
    empresa      VARCHAR(100) NOT NULL,
    data_inicio  DATE,
    data_fim     DATE,
    FOREIGN KEY (candidato_id) REFERENCES candidato (id)
);