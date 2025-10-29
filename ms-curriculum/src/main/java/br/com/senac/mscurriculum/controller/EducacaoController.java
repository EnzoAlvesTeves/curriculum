package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.service.EducacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/educacoes")
public class EducacaoController {
    private final EducacaoService educacaoService;

    public EducacaoController(EducacaoService educacaoService) {
        this.educacaoService = educacaoService;
    }

    @PostMapping
    public EducacaoDTO create(@RequestBody EducacaoDTO educacaoDTO) {
        return educacaoService.create(educacaoDTO);
    }

    @GetMapping("candidato/{candidatoId}")
    public List<EducacaoDTO> getByCandidatoId(@PathVariable Long candidatoId) {
        return educacaoService.getByCandidatoId(candidatoId);
    }

    @PutMapping
    public EducacaoDTO update(EducacaoDTO educacaoDTO) {
        return educacaoService.update(educacaoDTO);
    }

    @DeleteMapping("/{educacaoId}")
    public void delete(@PathVariable Long educacaoId) {
        educacaoService.delete(educacaoId);
    }
}
