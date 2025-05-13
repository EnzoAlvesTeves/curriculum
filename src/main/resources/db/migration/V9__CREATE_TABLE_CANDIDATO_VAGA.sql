CREATE TABLE candidato_vaga

(
    id INT AUTO_INCREMENT PRIMARY KEY,
    candidato_id INT NOT NULL,
    vaga_id INT NOT NULL,
    data_inscricao TIMESTAMP NOT NULL,
    FOREIGN KEY (candidato_id) REFERENCES candidato(id),
    FOREIGN KEY (vaga_id) REFERENCES vaga(id)
);