package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.disassemblers.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.CidadeNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncotradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/restaurantes")
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;

	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler; 

	@Override
    @GetMapping
    @CheckSecurity.restaurantes.PodeConsultar
    public CollectionModel<RestauranteBasicoModel> listar() {
        return restauranteBasicoModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }
	
	@Override
//	@JsonView(RestauranteView.ApenasNome.class)
	@CheckSecurity.restaurantes.PodeConsultar
    @GetMapping(params = "projecao=apenas-nome")
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return restauranteApenasNomeModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }
	

	@CheckSecurity.restaurantes.PodeConsultar
	@GetMapping(value ="{restauranteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		return restauranteModelAssembler.toModel(restaurante);
	}

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (RestauranteNaoEncotradoException | CidadeNaoEncotradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@PutMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {

		try {

			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(id);

			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (RestauranteNaoEncotradoException | CidadeNaoEncotradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@PutMapping(value="/{restauranteId}/ativo", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void>  ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@DeleteMapping(value = "/{restauranteId}/ativo", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public  ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@PutMapping(value = "/ativacoes", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.ativar(restaurantesIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncotradoException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	

	@CheckSecurity.restaurantes.PodeGerenciarCadastro
	@DeleteMapping(value = "/ativacoes", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.inativar(restaurantesIds);
			return ResponseEntity.noContent().build();
		} catch (RestauranteNaoEncotradoException e) {
			throw new NegocioException(e.getMessage());
		}

	}
	
	@CheckSecurity.restaurantes.PodeGerenciarFuncionamento
	@PutMapping(value = "/{restauranteId}/abertura", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.restaurantes.PodeGerenciarFuncionamento
	@PutMapping(value = "/{restauranteId}/fechamento", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}
}
