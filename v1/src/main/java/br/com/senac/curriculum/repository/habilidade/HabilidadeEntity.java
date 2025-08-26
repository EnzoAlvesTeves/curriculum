package br.com.senac.curriculum.repository.habilidade;


import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "habilidade")
public class HabilidadeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "descricao", nullable = false, length = 100)
	private String descricao;

	@Column(name = "nivel", length = 50)
	private String nivel;

	@Column(name = "especialidade", length = 100)
	private String especialidade;

	@ManyToOne
	@JoinColumn(name = "candidato_id")
	private CandidatoEntity candidato;

}
