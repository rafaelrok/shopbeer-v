package br.com.rafaelvieira.shopbeer.domain.dto;

import java.math.BigDecimal;

import br.com.rafaelvieira.shopbeer.domain.enums.Origin;
import lombok.Data;
import org.springframework.util.StringUtils;


@Data
public class BeerDTO {

	private Long code;
	private String sku;
	private String name;
	private String origin;
	private BigDecimal value;
	private String photo;
	private String urlThumbnailPhoto;

	public BeerDTO(Long code, String sku, String name, Origin origin, BigDecimal value, String photo) {
		this.code = code;
		this.sku = sku;
		this.name = name;
		this.origin = origin.getDescription();
		this.value = value;
		this.photo = StringUtils.isEmpty(photo) ? "beer-mock.png" : photo;
	}

}
