package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sale")
@DynamicUpdate
@NamedQueries({
		@NamedQuery(name = "sale.totalByMonth",
				query = "select year(sale.createDate) as year, month(sale.createDate) as month, count(*) as total " +
						"from Sale sale " +
						"where year(sale.createDate) * 12 + month(sale.createDate) >= year(current_date()) * 12 + month(current_date()) - 6 " +
						"and sale.status = 'ISSUED' " +
						"group by year(sale.createDate), month(sale.createDate) " +
						"order by year(sale.createDate) desc, month(sale.createDate) desc"),
		@NamedQuery(name = "sale.totalByOrigin",
				query = "select year(s.createDate) as year, month(s.createDate) as month, " +
						"(select coalesce(sum(i.quantity), 0) " +
						"from ItemSale i join i.beer b " +
						"where i.sale = s and b.origin = 'NATIONAL') as totalNational, " +
						"(select coalesce(sum(i.quantity), 0) " +
						"from ItemSale i join i.beer b " +
						"where i.sale = s and b.origin = 'INTERNATIONAL') as totalInternational " +
						"from Sale s " +
						"where year(s.createDate) * 12 + month(s.createDate) >= year(current_date()) * 12 + month(current_date()) - 6 " +
						"and s.status = 'ISSUED' " +
						"group by year(s.createDate), month(s.createDate) " +
						"order by year(s.createDate) desc, month(s.createDate) desc")
})
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@Column(name = "value_shipping")
	private BigDecimal valueShipping;

	@Column(name = "value_discount")
	private BigDecimal valueDiscount;

	private BigDecimal amount = BigDecimal.ZERO;

	private String observation;

	@Column(name = "date_time_delivery")
	private LocalDateTime dateTimeDelivery;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code_costumer")
	@ToString.Exclude
	private Costumer costumer;

	@ManyToOne
	@JoinColumn(name = "code_user_employee")
	private UserEmployee userEmployee;

	@Enumerated(EnumType.STRING)
	private StatusSale status = StatusSale.BUDGET;

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<ItemSale> itens = new ArrayList<>();

	@Transient
	private String uuid;

	@Transient
	private LocalDate deliveryDate;

	@Transient
	private LocalTime deliverySchedule;

	public boolean isNew() {
		return code == null;
	}
	
	public void addItems(List<ItemSale> itens) {
		this.itens = itens;
		this.itens.forEach(i -> i.setSale(this));
	}
	
	public BigDecimal getAmountItens() {
		return getItens().stream()
				.map(ItemSale::getAmount)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void calculateTotalValue() {
		this.amount = calculateTotalValue(getAmountItens(), getValueShipping(), getValueDiscount());
	}
	
	public Long getCreationDays() {
		LocalDate start = createDate != null ? createDate.toLocalDate() : LocalDate.now();
		return ChronoUnit.DAYS.between(start, LocalDate.now());
	}
	
	public boolean isSaveAllowed() {
		return !status.equals(StatusSale.CANCELED);
	}
	
	public boolean isSaveForbidden() {
		return !isSaveAllowed();
	}
	
	private BigDecimal calculateTotalValue(BigDecimal totalValueItems, BigDecimal valueShipping, BigDecimal valueDiscount) {
		BigDecimal amount = totalValueItems
				.add(Optional.ofNullable(valueShipping).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(valueDiscount).orElse(BigDecimal.ZERO));
		return amount;
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
		Sale other = (Sale) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
