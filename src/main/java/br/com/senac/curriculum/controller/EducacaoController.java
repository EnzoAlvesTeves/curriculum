package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.EducacaoDTO;
import br.com.senac.curriculum.service.EducacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/educacao")
public class EducacaoController {

	@Autowired
	private EducacaoService service;

	@PostMapping("/{candidatoId}")
	public EducacaoDTO cadastrar(@PathVariable Long candidatoId, @RequestBody EducacaoDTO educacaoDTO) {
		EducacaoDTO educacao = service.cadastrar(candidatoId, educacaoDTO);

		return educacao;
	}

	@PutMapping("/{educacaoId}")
	public EducacaoDTO editar(@PathVariable Long educacaoId, @RequestBody EducacaoDTO educacaoDTO) {
		EducacaoDTO educacao = service.editar(educacaoId, educacaoDTO);

		return educacao;
	}

	@DeleteMapping("/{educacaoId}")
	public void deletar(@PathVariable Long educacaoId) {
		service.deletar(educacaoId);
	}

}


