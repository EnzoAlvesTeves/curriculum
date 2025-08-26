package br.com.senac.curriculum.dto;

import br.com.senac.curriculum.enums.Sexo;
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

	private Long id;
	private String nome;
	private String email;
	private Sexo sexo;
	private String telefone;
	private LocalDate dataNascimento;
	private String resumoProfissional;
	private EnderecoDTO endereco;
	private UsuarioDTO usuario;
	private List<ExperienciaDTO> experiencias;
	private List<EducacaoDTO> educacoes;
	private List<HabilidadeDTO> habilidades;

	public CandidatoDTO() {
		this.experiencias = new ArrayList<>();
		this.educacoes = new ArrayList<>();
		this.habilidades = new ArrayList<>();
	}

	public CandidatoDTO(UsuarioDTO usuario) {
		this.usuario = usuario;
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.experiencias = new ArrayList<>();
		this.educacoes = new ArrayList<>();
		this.habilidades = new ArrayList<>();
	}

	public CandidatoDTO(CandidatoEntity candidato) {
		this.id = candidato.getId();
		this.nome = candidato.getNome();
		this.email = candidato.getEmail();
		this.sexo = candidato.getSexo();
		this.telefone = candidato.getTelefone();
		this.dataNascimento = candidato.getDataNascimento();
		this.resumoProfissional = candidato.getResumoProfissional();
		this.endereco = new EnderecoDTO(candidato.getEndereco());
		this.usuario = new UsuarioDTO(candidato.getUsuario());

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

	public CandidatoDTO(CandidatoEntity candidato, UsuarioDTO usuarioDTO) {
		this.id = candidato.getId();
		this.nome = candidato.getNome();
		this.email = candidato.getEmail();
		this.sexo = candidato.getSexo();
		this.telefone = candidato.getTelefone();
		this.dataNascimento = candidato.getDataNascimento();
		this.resumoProfissional = candidato.getResumoProfissional();
		this.endereco = new EnderecoDTO(candidato.getEndereco());
		this.usuario = usuarioDTO;

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
