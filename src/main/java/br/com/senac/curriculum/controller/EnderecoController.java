package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.EnderecoDTO;
import br.com.senac.curriculum.repository.endereco.EnderecoEntity;
import br.com.senac.curriculum.repository.endereco.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/endereco")
public class EnderecoController{

	@Autowired
	private EnderecoRepository repository;

	@PostMapping
	private EnderecoDTO salvar(@RequestBody EnderecoDTO enderecoDTO) {
		EnderecoEntity enderecoEntity = new EnderecoEntity();
		enderecoEntity.setRua(enderecoDTO.getRua());
		enderecoEntity.setNumero(enderecoDTO.getNumero());
		enderecoEntity.setComplemento(enderecoDTO.getComplemento());
		enderecoEntity.setCep(enderecoDTO.getCep());
		enderecoEntity.setCidade(enderecoDTO.getCidade());
		enderecoEntity.setEstado(enderecoDTO.getEstado());

		enderecoEntity = repository.save(enderecoEntity);

		enderecoDTO.setId(enderecoEntity.getId());
		return enderecoDTO;

	}
}
