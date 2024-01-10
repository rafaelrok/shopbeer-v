package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesMonthDTO {

	private String month;
	private Integer total;

	public SalesMonthDTO() {
	}

	public SalesMonthDTO(String month, Integer total) {
		this.month = month;
		this.total = total;
	}
}
