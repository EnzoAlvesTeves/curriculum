package br.com.senac.mscurriculum.repository.entity;

import jakarta.persistence.*;
import lombok.*;

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

	@Column(name = "rua", nullable = false, length = 500)
	private String rua;

	@Column(name = "numero")
	private String numero;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "cidade", nullable = false, length = 100)
	private String cidade;

	@Column(name = "estado",nullable = false, length = 2)
	private String estado;

	@Column(name = "cep", nullable = false, length = 10)
	private String cep;

	@Column(name = "bairro", nullable = false)
	private String bairro;

	@ManyToOne
	@JoinColumn(name = "candidato_id")
	private CandidatoEntity candidato;
}
