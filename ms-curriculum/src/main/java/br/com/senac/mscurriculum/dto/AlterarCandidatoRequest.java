package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para alterar os dados principais do candidato")
public class AlterarCandidatoRequest {
    @Schema(description = "Identificador do candidato", example = "1")
    private Long id;
    @Schema(description = "Identificador do usuário", example = "10")
    private Long idUsuario;
    @Schema(description = "Nome completo", example = "Guilherme Lopes")
    private String nome;
    @Schema(description = "E-mail", example = "guilherme@email.com")
    private String email;
    @Schema(description = "Sexo", example = "MASCULINO")
    private String sexo;
    @Schema(description = "Telefone", example = "11999999999")
    private String telefone;
    @Schema(description = "Data de nascimento", example = "1989-06-10")
    private LocalDate dataNascimento;
    @Schema(description = "Resumo profissional", example = "Desenvolvedor Java com experiência em microserviços")
    private String resumoProfissional;
}
