package br.com.rafaelvieira.shopbeer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "item_sale")
public class ItemSale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	private Integer quantity;

	@Column(name = "unitary_value")
	private BigDecimal unitaryValue;

	@ManyToOne
	@JoinColumn(name = "code_beer")
	private Beer beer;

	@ManyToOne
	@JoinColumn(name = "code_sale")
	private Sale sale;

	public BigDecimal getAmount() {
		return unitaryValue.multiply(new BigDecimal(quantity));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemSale other = (ItemSale) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
