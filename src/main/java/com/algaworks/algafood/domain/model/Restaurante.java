package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false) 
	private String nome;

	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@JoinColumn(name = "cozinha_id", nullable = false)
	@ManyToOne
	private Cozinha cozinha;
	
	@Embedded 
	private Endereco endereco;
	
	private Boolean ativo =  Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	@CreationTimestamp 
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime") 
	private OffsetDateTime data_atualizacao;
	
	@ManyToMany 
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name="restaurante_id"),
			inverseJoinColumns = @JoinColumn(name="forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamentos = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
				joinColumns = @JoinColumn(name = "restaurante_id"),
				inverseJoinColumns = @JoinColumn(name= "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamentos().remove(formaPagamento);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamentos().add(formaPagamento);
	}
	
	public void abrir() {
		setAberto(true);
	}
	
	public void fechar() {
		setAberto(false);
	}
	
	public boolean removeResponsavel(Usuario usuario) {
		return getResponsaveis().remove(usuario);
	}
	
	public boolean adcionarResponsavel(Usuario usuario) {
		return getResponsaveis().add(usuario);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return getFormasPagamentos().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return !aceitaFormaPagamento(formaPagamento);
	}
	
	public boolean isAberto() {
	    return this.aberto;
	}

	public boolean isFechado() {
	    return !isAberto();
	}

	public boolean isInativo() {
	    return !isAtivo();
	}

	public boolean isAtivo() {
	    return this.ativo;
	}

	public boolean aberturaPermitida() {
	    return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
	    return isInativo();
	}
	
	public boolean inativacaoPermitida() {
	    return isAtivo();
	}

	public boolean fechamentoPermitido() {
	    return isAberto();
	}    
}





