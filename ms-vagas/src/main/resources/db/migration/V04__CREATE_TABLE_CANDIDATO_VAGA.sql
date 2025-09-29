CREATE TABLE candidato_vaga (
    id INT AUTO_INCREMENT PRIMARY KEY,
    candidato_id INT NOT NULL,
    vaga_id INT NOT NULL,
    usuario_id INT NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (vaga_id) REFERENCES vaga(id)
);