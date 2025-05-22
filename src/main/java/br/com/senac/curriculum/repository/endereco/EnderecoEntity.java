package br.com.senac.curriculum.repository.endereco;

import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "endereco")
public class EnderecoEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rua", nullable = false, length = 250)
	private String rua;

	@Column(name = "numero")
	private int numero;

	@Column(name = "complemento",length = 255)
	private String complemento;

	@Column(name = "cidade",nullable = false, length = 100)
	private String cidade;

	@Column(name = "estado",nullable = false, length = 2)
	private String estado;

	@Column(name = "cep", nullable = false, length = 10)
	private String cep;

	@OneToMany(mappedBy = "endereco")
	private List<CandidatoEntity> candidatos;
	@Column(name = "bairro", nullable = false, length = 100)
	private String bairro;
}
