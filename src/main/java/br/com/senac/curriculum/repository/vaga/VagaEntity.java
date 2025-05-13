package br.com.senac.curriculum.repository.vaga;

import br.com.senac.curriculum.repository.candidatoVaga.CandidatoVagaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vaga")
public class VagaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "titulo", nullable = false, length = 255)
	private String titulo;

	@Column(name = "descricao", nullable = false, length = 500)
	private String descricao;

	@Column(name = "empresa", nullable = false, length = 255)
	private String empresa;

	@Column(name = "beneficios", length = 255)
	private String beneficios;

	@Column(name = "salario")
	private Double salario;

	@OneToMany(mappedBy = "vaga")
	private List<CandidatoVagaEntity> candidatoVagas;
}
