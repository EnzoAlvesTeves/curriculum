package br.com.senac.mscurriculum.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidato")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 100)
    private String sexo;

    @Column(length = 15)
    private String telefone;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "resumo_profissional", columnDefinition = "TEXT")
    private String resumoProfissional;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

