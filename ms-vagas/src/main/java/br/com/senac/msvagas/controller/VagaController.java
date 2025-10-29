package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.VagaDTO;
import br.com.senac.msvagas.service.VagaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vagas")
public class VagaController {
    private final VagaService vagaService;

    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @PostMapping
    public VagaDTO create(@RequestBody VagaDTO vagaDTO) {
        return vagaService.create(vagaDTO);
    }

    @PutMapping
    public VagaDTO update(VagaDTO vagaDTO) {
        return vagaService.update(vagaDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vagaService.delete(id);
    }

    @GetMapping("/{id}")
    public  VagaDTO getById(@PathVariable Long id) {
        return vagaService.getById(id);
    }

    @GetMapping
    public List<VagaDTO> getall() {
        return vagaService.getall();
    }

}
