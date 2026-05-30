package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;

public final class CandidatoMapper {

    private CandidatoMapper() {
    }

    public static CandidatoDTO toDTO(CandidatoEntity entity) {
        if (entity == null) {
            return null;
        }

        CandidatoDTO dto = new CandidatoDTO();
        dto.setId(entity.getId());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setSexo(entity.getSexo());
        dto.setTelefone(entity.getTelefone());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setResumoProfissional(entity.getResumoProfissional());

        return dto;
    }
}

