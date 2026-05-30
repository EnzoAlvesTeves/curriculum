package br.com.senac.mscurriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
    private Long id;
    private Long idCandidato;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private String bairro;
    private BigDecimal latitude;
    private BigDecimal longitude;
}

