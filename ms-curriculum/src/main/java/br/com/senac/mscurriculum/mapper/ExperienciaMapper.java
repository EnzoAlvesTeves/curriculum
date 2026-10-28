package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;

public final class ExperienciaMapper {

    private ExperienciaMapper() {
    }

    public static ExperienciaDTO toDTO(ExperienciaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ExperienciaDTO(
                entity.getId(),
                entity.getIdCandidato(),
                entity.getCargo(),
                entity.getEmpresa(),
                entity.getResumo(),
                entity.getDataInicio(),
                entity.getDataFim()
        );
    }

    public static ExperienciaEntity toEntity(ExperienciaDTO dto, Long candidatoId) {
        if (dto == null) {
            return null;
        }

        ExperienciaEntity entity = new ExperienciaEntity();
        entity.setId(dto.getId());
        entity.setIdCandidato(candidatoId);
        entity.setCargo(dto.getCargo());
        entity.setEmpresa(dto.getEmpresa());
        entity.setResumo(dto.getResumo());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());

        return entity;
    }
}

