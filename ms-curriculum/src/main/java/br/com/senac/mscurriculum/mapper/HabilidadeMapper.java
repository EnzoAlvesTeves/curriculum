package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;

public final class HabilidadeMapper {

    private HabilidadeMapper() {
    }

    public static HabilidadeDTO toDTO(HabilidadeEntity entity) {
        if (entity == null) {
            return null;
        }

        return new HabilidadeDTO(
                entity.getId(),
                entity.getIdCandidato(),
                entity.getDescricao(),
                entity.getNivel()
        );
    }

    public static HabilidadeEntity toEntity(HabilidadeDTO dto, Long candidatoId) {
        if (dto == null) {
            return null;
        }

        HabilidadeEntity entity = new HabilidadeEntity();
        entity.setId(dto.getId());
        entity.setIdCandidato(candidatoId);
        entity.setDescricao(dto.getDescricao());
        entity.setNivel(dto.getNivel());

        return entity;
    }
}

