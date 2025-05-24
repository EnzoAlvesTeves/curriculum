package br.com.senac.curriculum.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidaturaRequest {
    private Long candidatoId;
    private Long vagaId;
}
