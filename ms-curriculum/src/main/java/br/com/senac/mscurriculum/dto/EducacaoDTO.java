package br.com.senac.mscurriculum.dto;

import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducacaoDTO {

    private Long id;
    private String grau;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String instituicao;
    private String curso;
    private Long candidatoId;

    public EducacaoDTO(EducacaoEntity educacaoEntity) {
        this.id = educacaoEntity.getId();
        this.grau = educacaoEntity.getGrau();
        this.dataInicio = educacaoEntity.getDataInicio();
        this.dataFim = educacaoEntity.getDataFim();
        this.instituicao = educacaoEntity.getInstituicao();
        this.curso = educacaoEntity.getCurso();
    }

    public EducacaoDTO(EducacaoEntity educacao, Long candidatoId) {
        this.id = educacao.getId();
        this.grau = educacao.getGrau();
        this.dataInicio = educacao.getDataInicio();
        this.dataFim = educacao.getDataFim();
        this.instituicao = educacao.getInstituicao();
        this.curso = educacao.getCurso();
        this.candidatoId = candidatoId;
    }

    public EducacaoEntity toEntity() {
        EducacaoEntity educacaoEntity = new EducacaoEntity();
        educacaoEntity.setInstituicao(this.instituicao);
        educacaoEntity.setCurso(this.curso);
        educacaoEntity.setDataInicio(this.dataInicio);
        educacaoEntity.setDataFim(this.dataFim);
        educacaoEntity.setGrau(this.grau);
        educacaoEntity.setId(this.id);
        return educacaoEntity;
    }
}
