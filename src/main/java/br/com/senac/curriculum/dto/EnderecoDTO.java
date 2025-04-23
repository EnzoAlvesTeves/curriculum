package br.com.senac.curriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

	private Long	 id;
	private String rua;
	private int numero;
	private String complemento;
	private String cidade;
	private String estado;
	private String cep;
}
