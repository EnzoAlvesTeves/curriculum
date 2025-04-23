package br.com.senac.curriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabilidadeDTO {

	private Long id;
	private String descricao;
	private String nivel;
	private String especialidade;
}
