package br.com.senac.mscurriculum.dto;

import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private String bairro;

    public EnderecoDTO(EnderecoEntity enderecoEntity) {
        this.id = enderecoEntity.getId();
        this.rua = enderecoEntity.getRua();
        this.numero = enderecoEntity.getNumero();
        this.complemento = enderecoEntity.getComplemento();
        this.cidade = enderecoEntity.getCidade();
        this.estado = enderecoEntity.getEstado();
        this.cep = enderecoEntity.getCep();
        this.bairro = enderecoEntity.getBairro();
    }
}