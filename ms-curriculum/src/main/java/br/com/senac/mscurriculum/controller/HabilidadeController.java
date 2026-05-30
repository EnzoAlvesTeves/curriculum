package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.service.HabilidadeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidatos/{candidatoId}/habilidades")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class HabilidadeController {

    private final HabilidadeService habilidadeService;

}
