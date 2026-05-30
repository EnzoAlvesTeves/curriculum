package br.com.senac.mscurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterarCandidatoRequest {
    private Long id;
    private Long idUsuario;
    private String nome;
    private String email;
    private String sexo;
    private String telefone;
    private LocalDate dataNascimento;
    private String resumoProfissional;
}
