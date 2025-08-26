package br.com.senac.curriculum.repository.candidato;


import br.com.senac.curriculum.enums.Sexo;
import br.com.senac.curriculum.repository.candidatoVaga.CandidatoVagaEntity;
import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import br.com.senac.curriculum.repository.endereco.EnderecoEntity;
import br.com.senac.curriculum.repository.experiencia.ExperienciaEntity;
import br.com.senac.curriculum.repository.habilidade.HabilidadeEntity;
import br.com.senac.curriculum.repository.usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

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

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "sexo", nullable = false)
	@Enumerated(EnumType.STRING)
	private Sexo sexo;

	@Column(name = "telefone", length = 15)
	private String telefone;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Column(name = "resumo_profissional", length = 1000)
	private String resumoProfissional;

	@ManyToOne
	@JoinColumn(name = "endereco_id")
	private EnderecoEntity endereco;

	@OneToOne
	@JoinColumn(name = "usuario_id")
	private UsuarioEntity usuario;

	@OneToMany(mappedBy = "candidato")
	private List<ExperienciaEntity> experiencias;

	@OneToMany(mappedBy = "candidato")
	private List<EducacaoEntity> educacoes;

	@OneToMany(mappedBy = "candidato")
	private List<HabilidadeEntity> habilidades;

	@OneToMany(mappedBy = "candidato")
	private List<CandidatoVagaEntity> candidatoVagas;
}
