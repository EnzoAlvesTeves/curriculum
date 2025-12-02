package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CandidatoVagaDTO;
import br.com.senac.msvagas.service.CandidatoVagaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidato-vaga")
public class CandidatoVagaController {
    private final CandidatoVagaService candidatoVagaService;

    public CandidatoVagaController(
            CandidatoVagaService candidatoVagaService
    ) {
        this.candidatoVagaService = candidatoVagaService;
    }

    @PostMapping
    public CandidatoVagaDTO create(@RequestBody CandidatoVagaDTO candidatoVagaDTO) {
        return candidatoVagaService.create(candidatoVagaDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        candidatoVagaService.delete(id);
    }

    @GetMapping("/candidato/{candidatoId}")
    public List<CandidatoVagaDTO> getByCandidatoId(@PathVariable Long candidatoId) {
        return candidatoVagaService.getByCandidatoId(candidatoId);
    }

    @GetMapping("/vaga/{vagaId}")
    public List<CandidatoVagaDTO> getByVagaId(@PathVariable Long vagaId) {
        return candidatoVagaService.getByVagaId(vagaId);
    }
}
