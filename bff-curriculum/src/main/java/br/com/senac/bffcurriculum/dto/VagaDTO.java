package br.com.senac.bffcurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VagaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String empresa;
    private String beneficios;
    private double salario;
}
