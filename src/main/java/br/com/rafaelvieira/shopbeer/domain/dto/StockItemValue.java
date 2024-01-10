package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockItemValue {

	private BigDecimal value;
	private Long totalItens;
	
	public StockItemValue() {
	}

	public StockItemValue(BigDecimal value, Long totalItens) {
		this.value = value;
		this.totalItens = totalItens;
	}

	public BigDecimal getValue() {
		return value != null ? value : BigDecimal.ZERO;
	}

	public Long getTotalItens() {
		return totalItens != null ? totalItens : 0L;
	}
}
