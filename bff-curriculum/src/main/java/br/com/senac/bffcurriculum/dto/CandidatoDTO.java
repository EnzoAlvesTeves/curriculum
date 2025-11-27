package br.com.senac.bffcurriculum.dto;

import br.com.senac.bffcurriculum.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private List<EducacaoDTO> educacao;
    private List<ExperienciaDTO> experiencia;
    private List<HabilidadeDTO> habilidade;

    private UsuarioDTO usuario;

    public CandidatoDTO(UsuarioDTO usuarioDTO) {
        this.usuarioId = usuarioDTO.getId();
        this.usuario = usuarioDTO;
        this.nome = usuarioDTO.getNome();
        this.email = usuarioDTO.getEmail();

        this.experiencia = new ArrayList<>();
        this.educacao = new ArrayList<>();
        this.habilidade = new ArrayList<>();
    }
}
