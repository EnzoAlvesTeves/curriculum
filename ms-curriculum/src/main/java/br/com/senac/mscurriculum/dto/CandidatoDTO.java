package br.com.senac.mscurriculum.dto;

import br.com.senac.mscurriculum.enums.Sexo;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String resumoProfissional;
    private Long usuarioId;
    private EnderecoDTO endereco;

    private List<EducacaoDTO> educacoes;
    private List<ExperienciaDTO> experiencias;
    private List<HabilidadeDTO> habilidades;

    public CandidatoDTO(CandidatoEntity entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.telefone = entity.getTelefone();
        this.sexo = entity.getSexo();
        this.dataNascimento = entity.getDataNascimento();
        this.resumoProfissional = entity.getResumoProfissional();
        this.usuarioId = entity.getUsuarioId();

        if (entity.getEndereco() != null) {
            this.endereco = new EnderecoDTO(entity.getEndereco());
        }

        if (entity.getEducacoes() != null) {
            this.educacoes = entity.getEducacoes().stream()
                    .map(educacao -> new EducacaoDTO(educacao, this.id))
                    .toList();
        }

        if (entity.getExperiencias() != null) {
            this.experiencias = entity.getExperiencias().stream()
                    .map(experiencia -> new ExperienciaDTO(experiencia, this.id))
                    .toList();
        }

        if (entity.getHabilidades() != null) {
            this.habilidades = entity.getHabilidades().stream()
                    .map(habilidade -> new HabilidadeDTO(habilidade, this.id))
                    .toList();
        }
    }
}
