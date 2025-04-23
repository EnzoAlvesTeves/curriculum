package br.com.senac.curriculum.repository.educacao;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "educacao")
public class EducacaoEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "grau", nullable = false, length = 50)
	private String grau;

	@Column(name = "data_inicio")
	private LocalDate dataInicio;

	@Column(name = "data_fim")
	private LocalDate dataFim;

	@Column(name = "instituicao", nullable = false, length = 100)
	private String instituicao;

	@Column(name = "curso", nullable = false, length = 100)
	private String curso;

	@ManyToOne
	@JoinColumn(name = "candidato_id")
	private CandidatoEntity candidato;

}
