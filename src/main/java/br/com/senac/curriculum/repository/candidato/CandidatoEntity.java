package br.com.senac.curriculum.repository.candidato;


import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import br.com.senac.curriculum.repository.endereco.EnderecoEntity;
import br.com.senac.curriculum.repository.experiencia.ExperienciaEntity;
import br.com.senac.curriculum.repository.habilidade.HabilidadeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidato")
public class CandidatoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", nullable = false, length = 255)
	private String nome;

	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "sexo", nullable = false)
	private String sexo;

	@Column(name = "telefone", length = 15)
	private String telefone;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@ManyToOne
	@JoinColumn(name = "endereco_id")
	private EnderecoEntity endereco;

	@OneToMany(mappedBy = "candidato")
	private List<ExperienciaEntity> experiencias;

	@OneToMany(mappedBy = "candidato")
	private List<EducacaoEntity> educacoes;

	@OneToMany(mappedBy = "candidato")
	private List<HabilidadeEntity> habilidades;

}
