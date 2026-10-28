package br.com.senac.mscurriculum.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habilidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabilidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_candidato", nullable = false)
    private Long idCandidato;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(length = 50)
    private String nivel;
}
