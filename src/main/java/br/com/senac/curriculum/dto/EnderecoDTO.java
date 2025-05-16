package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.endereco.EnderecoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

	private Long id;
	private String rua;
	private int numero;
	private String complemento;
	private String cidade;
	private String estado;
	private String cep;

	public EnderecoDTO(EnderecoEntity endereco) {
		this.id = endereco.getId();
		this.rua = endereco.getRua();
		this.numero = endereco.getNumero();
		this.complemento = endereco.getComplemento();
		this.cidade = endereco.getCidade();
		this.estado = endereco.getEstado();
		this.cep = endereco.getCep();
	}
}
