package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.usuario.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private CandidatoDTO candidato;

	public UsuarioDTO(UsuarioEntity entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.senha = entity.getSenha();
		if (entity.getCandidato() != null) {
			this.candidato = new CandidatoDTO(entity.getCandidato());
		}
	}
}
