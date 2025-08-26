package br.com.senac.curriculum.repository.usuario;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class UsuarioEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "email", updatable = false, nullable = false)
	private String email;

	@Column(name = "senha", nullable = false)
	private String senha;

	@OneToOne(mappedBy = "usuario")
	private CandidatoEntity candidato;
}
