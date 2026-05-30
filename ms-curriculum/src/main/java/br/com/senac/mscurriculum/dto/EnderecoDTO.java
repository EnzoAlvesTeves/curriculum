package br.com.senac.mscurriculum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Endereço vinculado ao candidato")
public class EnderecoDTO {
    @Schema(description = "Identificador do endereço", example = "1")
    private Long id;
    @Schema(description = "Identificador do candidato", example = "1")
    private Long idCandidato;
    @Schema(description = "Rua", example = "Av. da casa dele")
    private String rua;
    @Schema(description = "Número", example = "666")
    private String numero;
    @Schema(description = "Complemento", example = "Apto 123, torre 1")
    private String complemento;
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;
    @Schema(description = "Estado", example = "São Paulo")
    private String estado;
    @Schema(description = "CEP", example = "09999-000")
    private String cep;
    @Schema(description = "Bairro", example = "Centro")
    private String bairro;
    @Schema(description = "Latitude", example = "-23.69344148866667")
    private BigDecimal latitude;
    @Schema(description = "Longitude", example = "-46.62110583766665")
    private BigDecimal longitude;
}

