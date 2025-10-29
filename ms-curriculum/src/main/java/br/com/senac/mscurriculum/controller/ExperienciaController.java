package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.service.ExperienciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiencias")
public class ExperienciaController {
    private final ExperienciaService experienciaService;

    public ExperienciaController(ExperienciaService experienciaService) {
        this.experienciaService = experienciaService;
    }

    @PostMapping
    public ExperienciaDTO create(@RequestBody ExperienciaDTO experienciaDTO) {
        return experienciaService.create(experienciaDTO);
    }

    @GetMapping("candidato/{candidatoId}")
    public List<ExperienciaDTO> getByCandidatoId(@PathVariable Long candidatoId) {
        return experienciaService.getByCandidatoId(candidatoId);
    }

    @PutMapping
    public ExperienciaDTO update(@RequestBody ExperienciaDTO experienciaDTO) {
        return experienciaService.update(experienciaDTO);
    }

    @DeleteMapping("/{experienciaId}")
    public void delete(@PathVariable Long experienciaId) {
       experienciaService.delete(experienciaId);
    }
}
