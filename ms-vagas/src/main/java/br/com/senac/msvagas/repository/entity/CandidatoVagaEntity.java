package br.com.senac.msvagas.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidato_vaga")
public class CandidatoVagaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_inscricao", nullable = false)
	private LocalDateTime dataInscricao;

	@Column(name = "candidato_id")
	private Long candidatoId;

	@ManyToOne
	@JoinColumn(name = "vaga_id")
	private VagaEntity vaga;

}
