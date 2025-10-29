package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.service.EnderecoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public EnderecoDTO create(@RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.create(enderecoDTO);
    }

    @PutMapping
    public EnderecoDTO update(@RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.update(enderecoDTO);
    }

    @GetMapping("/{id}")
    public EnderecoDTO getById(@PathVariable Long id) {
        return enderecoService.getById(id);
    }

    @DeleteMapping("/{enderecoId}")
    public void delete(@PathVariable Long enderecoId) {
        enderecoService.delete(enderecoId);
    }
}
