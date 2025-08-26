package br.com.senac.curriculum.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sexo {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    OUTRO("Outro");

    private final String descricao;

    public static Sexo fromDescricao(String descricao) {
        for (Sexo sexo : Sexo.values()) {
            if (sexo.getDescricao().equalsIgnoreCase(descricao)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Sexo inválido: " + descricao);
    }
}
