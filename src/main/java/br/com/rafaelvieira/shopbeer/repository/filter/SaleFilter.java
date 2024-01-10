package br.com.rafaelvieira.shopbeer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleFilter {

	private Long code;
	private StatusSale statusSale;
	private LocalDate since;
	private LocalDate until;
	private BigDecimal minimumValue;
	private BigDecimal maximumValue;
	private String costumerName;
	private String cpforCnpjCostumer;
}
