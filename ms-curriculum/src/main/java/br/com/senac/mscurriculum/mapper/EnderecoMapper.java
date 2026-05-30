package br.com.senac.mscurriculum.mapper;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;

public final class EnderecoMapper {

    private EnderecoMapper() {
    }

    public static EnderecoDTO toDTO(EnderecoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new EnderecoDTO(
                entity.getId(),
                entity.getIdCandidato(),
                entity.getRua(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep(),
                entity.getBairro(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }

    public static EnderecoEntity toEntity(EnderecoDTO dto, Long candidatoId) {
        if (dto == null) {
            return null;
        }

        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(dto.getId());
        entity.setIdCandidato(candidatoId);
        entity.setRua(dto.getRua());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        entity.setCep(dto.getCep());
        entity.setBairro(dto.getBairro());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());

        return entity;
    }
}

