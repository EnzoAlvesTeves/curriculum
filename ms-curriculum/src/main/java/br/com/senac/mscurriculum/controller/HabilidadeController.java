package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.service.HabilidadeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habilidades")
public class HabilidadeController {
    private final HabilidadeService habilidadeService;

    public HabilidadeController(HabilidadeService habilidadeService) {
        this.habilidadeService = habilidadeService;
    }

    @PostMapping
    public HabilidadeDTO create(@RequestBody HabilidadeDTO habilidadeDTO) {
        return habilidadeService.create(habilidadeDTO);
    }

    @GetMapping("candidato/{candidatoId}")
    public List<HabilidadeDTO> getByCandidatoId(@PathVariable Long candidatoId) {
        return habilidadeService.getByCandidatoId(candidatoId);
    }

    @PutMapping
    public HabilidadeDTO update(@RequestBody HabilidadeDTO habilidadeDTO) {
        return habilidadeService.update(habilidadeDTO);
    }

    @DeleteMapping("/{habilidadeId}")
    public void delete(@PathVariable Long habilidadeId) {
        habilidadeService.delete(habilidadeId);
    }
}
