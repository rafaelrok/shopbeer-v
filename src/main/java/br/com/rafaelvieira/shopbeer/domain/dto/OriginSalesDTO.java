package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OriginSalesDTO {

	private LocalDateTime month;
	private Integer totalNational;
	private Integer totalInternational;
	
	public OriginSalesDTO() {
		
	}

	public OriginSalesDTO(LocalDateTime month, Integer totalNational, Integer totalInternational) {
		this.month = month;
		this.totalNational = totalNational;
		this.totalInternational = totalInternational;
	}
}
