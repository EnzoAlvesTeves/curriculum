package br.com.senac.msvagas.dto;

public enum TipoUsuario {
    RH,
    CANDIDATO;

    public boolean ehRh() {
        return this == RH;
    }

    public boolean ehCandidato() {
        return this == CANDIDATO;
    }
}
