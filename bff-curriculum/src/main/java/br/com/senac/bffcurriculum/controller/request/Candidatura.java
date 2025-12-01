package br.com.senac.bffcurriculum.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidatura {
    private Long candidatoId;
    private Long vagaId;
}
