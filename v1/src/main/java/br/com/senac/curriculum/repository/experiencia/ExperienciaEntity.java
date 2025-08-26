package br.com.senac.curriculum.repository.experiencia;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "experiencia")
public class ExperienciaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "cargo", nullable = false, length = 100)
	private String cargo;

	@Column(name = "empresa", nullable = false, length = 100)
	private String empresa;

	@Column(name = "data_inicio")
	private LocalDate dataInicio;

	@Column(name = "data_fim")
	private LocalDate dataFim;

	@ManyToOne
	@JoinColumn(name = "candidato_id")
	private CandidatoEntity candidato;

}
