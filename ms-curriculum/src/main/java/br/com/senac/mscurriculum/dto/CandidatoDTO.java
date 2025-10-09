package br.com.senac.mscurriculum.dto;

import java.time.LocalDate;

import br.com.senac.mscurriculum.repository.CandidatoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO {
	private Long id;
	private String nome;
	private String email;
	private String telefone;
	private LocalDate dataNascimento;
	private String resumoProfissional;
	private Long usuarioId;
	private EnderecoDTO endereco;


	public CandidatoDTO(CandidatoEntity entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.telefone = entity.getTelefone();
		this.dataNascimento = entity.getDataNascimento();
		this.resumoProfissional = entity.getResumoProfissional();
		this.usuarioId = entity.getUsuarioId();
		if (entity.getEndereco() != null) {
			this.endereco = new EnderecoDTO(entity.getEndereco());
		}
	}
	}
