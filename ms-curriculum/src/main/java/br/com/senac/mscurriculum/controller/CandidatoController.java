package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.service.CandidatoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidatos")
public class CandidatoController {
    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService) {
       this.candidatoService = candidatoService;
    }

    @PostMapping
    public CandidatoDTO create(@RequestBody CandidatoDTO candidatoDTO) {
        return candidatoService.create(candidatoDTO);
    }

    @GetMapping("/{id}")
    public CandidatoDTO getById(@PathVariable Long id) {
        return candidatoService.getById(id);
    }

    @GetMapping
    public List<CandidatoDTO> getAll() {
        return candidatoService.getAll();
    }

    @PutMapping
    public CandidatoDTO update(CandidatoDTO candidatoDTO) {
        return candidatoService.update(candidatoDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        candidatoService.delete(id);
    }
}
