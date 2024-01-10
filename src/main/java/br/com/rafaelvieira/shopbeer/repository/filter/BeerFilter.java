package br.com.rafaelvieira.shopbeer.repository.filter;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.domain.enums.Flavor;
import br.com.rafaelvieira.shopbeer.domain.enums.Origin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BeerFilter {

	private String sku;
	private String name;
	private Style style;
	private Flavor flavor;
	private Origin origin;
	private BigDecimal valueOf;
	private BigDecimal valueUpTo;

}
