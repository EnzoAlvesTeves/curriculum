package br.com.senac.mscurriculum.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "educacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_candidato", nullable = false)
    private Long idCandidato;

    @Column(nullable = false, length = 100)
    private String curso;

    @Column(nullable = false, length = 100)
    private String grau;

    @Column(nullable = false, length = 100)
    private String instituicao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;
}
