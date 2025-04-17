package br.com.senac.curriculum.repository.endereco;

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
	private int id;

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
}
