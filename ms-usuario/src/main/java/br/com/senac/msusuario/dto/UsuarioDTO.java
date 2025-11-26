package br.com.senac.msusuario.dto;

import br.com.senac.msusuario.repository.entity.UsuarioEntity;
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

	public UsuarioDTO(UsuarioEntity entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.senha = entity.getSenha();
	}

	public UsuarioEntity toEntity() {
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setId(this.id);
		usuarioEntity.setNome(this.nome);
		usuarioEntity.setEmail(this.email);
		usuarioEntity.setSenha(this.senha);
		return usuarioEntity;
	}

}
