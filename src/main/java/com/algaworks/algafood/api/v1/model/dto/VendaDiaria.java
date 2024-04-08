package com.algaworks.algafood.api.v1.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaDiaria {

	private Date data;
	private Long totalVendas;
	private BigDecimal totalFatura;

	public VendaDiaria(java.sql.Date data, Long totalVendas, BigDecimal totalFatura) {
		this.data = new Date(data.getTime());
		this.totalVendas = totalVendas;
		this.totalFatura = totalFatura;
	}
}