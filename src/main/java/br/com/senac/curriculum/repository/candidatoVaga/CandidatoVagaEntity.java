package br.com.senac.curriculum.repository.candidatoVaga;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cadidatoVaga")
public class CandidatoVagaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_inscricao", nullable = false)
	private LocalDateTime dataInscricao;

	@ManyToOne
	@JoinColumn(name = "candidato_id")
	private CandidatoEntity candidato;

	@ManyToOne
	@JoinColumn(name = "vaga_id")
	private VagaEntity vaga;

}
