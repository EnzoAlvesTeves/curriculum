package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;

public final class EducacaoMapper {

    private EducacaoMapper() {
    }

    public static EducacaoDTO toDTO(EducacaoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new EducacaoDTO(
                entity.getId(),
                entity.getIdCandidato(),
                entity.getCurso(),
                entity.getGrau(),
                entity.getInstituicao(),
                entity.getDataInicio(),
                entity.getDataFim()
        );
    }

    public static EducacaoEntity toEntity(EducacaoDTO dto, Long candidatoId) {
        if (dto == null) {
            return null;
        }

        EducacaoEntity entity = new EducacaoEntity();
        entity.setId(dto.getId());
        entity.setIdCandidato(candidatoId);
        entity.setCurso(dto.getCurso());
        entity.setGrau(dto.getGrau());
        entity.setInstituicao(dto.getInstituicao());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());

        return entity;
    }
}

