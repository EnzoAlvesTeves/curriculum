package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.service.EducacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidatos/{candidatoId}/educacoes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EducacaoController {

    private final EducacaoService educacaoService;

}
