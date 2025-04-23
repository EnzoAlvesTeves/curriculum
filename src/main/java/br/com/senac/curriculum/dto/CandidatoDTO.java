package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CandidatoDTO {

	public CandidatoDTO() {
		this.experiencias = new ArrayList<>();
		this.educacoes = new ArrayList<>();
		this.habilidades = new ArrayList<>();
	}

	private Long id;
	private String nome;
	private String email;
	private String sexo;
	private String telefone;
	private LocalDate dataNascimento;
	private EnderecoDTO endereco;
	private List<ExperienciaDTO> experiencias;
	private List<EducacaoDTO> educacoes;
	private List<HabilidadeDTO> habilidades;
}
