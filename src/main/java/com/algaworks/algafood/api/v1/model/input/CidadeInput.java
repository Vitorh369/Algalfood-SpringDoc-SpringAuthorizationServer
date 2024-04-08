package com.algaworks.algafood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CidadeInput {

	@Schema(example = "Uberlândia")
	@NotBlank
	private String nome;
	
	@Valid
	@NonNull
	private EstadoIdInput estado;
}
