package br.com.senac.mscurriculum.dto;

public enum TipoUsuario {
    RH,
    CANDIDATO;

    public boolean ehCandidato() {
        return this == CANDIDATO;
    }
}
