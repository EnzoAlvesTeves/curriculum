package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

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

	public CandidatoDTO(CandidatoEntity candidato) {
		this.id = candidato.getId();
		this.nome = candidato.getNome();
		this.email = candidato.getEmail();
		this.sexo = candidato.getSexo();
		this.telefone = candidato.getTelefone();
		this.dataNascimento = candidato.getDataNascimento();
		this.endereco = new EnderecoDTO(candidato.getEndereco());

		List<ExperienciaDTO> experiencias = new ArrayList<>();
		candidato.getExperiencias().forEach(expecienciaEntity -> {
			ExperienciaDTO experienciaDTO = new ExperienciaDTO(expecienciaEntity);
			experiencias.add(experienciaDTO);
		});
		this.experiencias = experiencias;

		List<EducacaoDTO> educacoes = new ArrayList<>();
		candidato.getEducacoes().forEach(educacaoEntity -> {
			EducacaoDTO educacaoDTO = new EducacaoDTO(educacaoEntity);
			educacoes.add(educacaoDTO);
		});
		this.educacoes = educacoes;

		List<HabilidadeDTO> habilidades = new ArrayList<>();
		candidato.getHabilidades().forEach(habilidadeEntity -> {
			HabilidadeDTO habilidadeDTO = new HabilidadeDTO(habilidadeEntity);
			habilidades.add(habilidadeDTO);
		});
		this.habilidades = habilidades;

	}
}
