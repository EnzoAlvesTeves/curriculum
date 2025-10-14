package br.com.senac.mscurriculum.repository.entity;

import br.com.senac.mscurriculum.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

	@Column(name = "telefone", length = 15)
	private String telefone;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Column(name = "resumo_profissional", length = 1000)
	private String resumoProfissional;

	@ManyToOne
	@JoinColumn(name = "endereco_id")
	private EnderecoEntity endereco;

	@Column(name = "usuario_id", nullable = false)
	private Long usuarioId;

	@Column(name = "sexo", nullable = false)
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
}

