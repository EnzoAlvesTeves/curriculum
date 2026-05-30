package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa o candidato com endereço e listas relacionadas")
public class CandidatoDTO {
    @Schema(description = "Identificador do candidato", example = "1")
    private Long id;
    @Schema(description = "Identificador do usuário autenticado vinculado ao candidato", example = "10")
    private Long idUsuario;
    @Schema(description = "Nome completo do candidato", example = "João da Silva")
    private String nome;
    @Schema(description = "E-mail do candidato", example = "joao@email.com")
    private String email;
    @Schema(description = "Sexo do candidato", example = "MASCULINO")
    private String sexo;
    @Schema(description = "Telefone do candidato", example = "11999999999")
    private String telefone;
    @Schema(description = "Data de nascimento", example = "1989-12-31")
    private LocalDate dataNascimento;
    @Schema(description = "Resumo profissional do candidato", example = "Desenvolvedor Java com experiência em microserviços")
    private String resumoProfissional;
    @Schema(description = "Endereço do candidato")
    private EnderecoDTO endereco;
    @Schema(description = "Lista de educações do candidato")
    private List<EducacaoDTO> educacoes = new ArrayList<>();
    @Schema(description = "Lista de experiências do candidato")
    private List<ExperienciaDTO> experiencias = new ArrayList<>();
    @Schema(description = "Lista de habilidades do candidato")
    private List<HabilidadeDTO> habilidades = new ArrayList<>();
}
