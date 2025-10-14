package br.com.senac.bffcurriculum.dto;

import br.com.senac.bffcurriculum.enums.Sexo;

import java.time.LocalDate;

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
}
