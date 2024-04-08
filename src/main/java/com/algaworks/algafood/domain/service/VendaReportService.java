package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

	byte[] emitirVendaDiarias(VendaDiariaFilter filtro, String timeOffset);
}
