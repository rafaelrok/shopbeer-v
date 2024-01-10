package br.com.rafaelvieira.shopbeer.repository.query.sale;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.domain.dto.OriginSalesDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.SalesMonthDTO;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import br.com.rafaelvieira.shopbeer.repository.filter.SaleFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class SalesImpl implements SalesQuery {

	@PersistenceContext
	private EntityManager manager;

	private final PaginationUtil paginationUtil;

	public SalesImpl(PaginationUtil paginationUtil) {
		this.paginationUtil = paginationUtil;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Sale> findByUuid(SaleFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Sale> query = builder.createQuery(Sale.class);
		Root<Sale> root = query.from(Sale.class);

		addFilter(filter, builder, root);

		TypedQuery<Sale> typedQuery = manager.createQuery(query);
		paginationUtil.prepare(builder, pageable);

		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filter));
	}

	@Transactional(readOnly = true)
	@Override
	public Sale findByCode(Long code) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Sale> query = builder.createQuery(Sale.class);
		Root<Sale> root = query.from(Sale.class);
		root.fetch("items", JoinType.LEFT);

		Predicate predicate = builder.equal(root.get("code"), code);
		query.where(predicate);

		return manager.createQuery(query).getSingleResult();
	}
	
	@Override
	public BigDecimal totalValueInYear() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(amount) from Sale where year(createDate) = :year and status = :status", BigDecimal.class)
					.setParameter("year", Year.now().getValue())
					.setParameter("status", StatusSale.ISSUED)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal totalValueInMonth() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(amount) from Sale where month(createDate) = :month and status = :status", BigDecimal.class)
					.setParameter("month", MonthDay.now().getMonthValue())
					.setParameter("status", StatusSale.ISSUED)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal averageTicketValueInYear() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(amount)/count(*) from Sale where year(createDate) = :year and status = :status", BigDecimal.class)
					.setParameter("year", Year.now().getValue())
					.setParameter("status", StatusSale.ISSUED)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SalesMonthDTO> totalByMonth() {
		List<SalesMonthDTO> salesMonth = manager.createNamedQuery("sale.totalByMonth").getResultList();

		LocalDate today = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String idealMonth = String.format("%d/%02d", today.getYear(), today.getMonthValue());

			boolean hasMonth = salesMonth.stream().anyMatch(v -> v.getMonth().equals(idealMonth));
			if (!hasMonth) {
				salesMonth.add(i - 1, new SalesMonthDTO(idealMonth, 0));
			}

			today = today.minusMonths(1);
		}

		return salesMonth;
	}

	@Override
	public List<OriginSalesDTO> totalByOrigin() {
		List<OriginSalesDTO> salesNationality = manager.createNamedQuery("sale.totalByOrigin", OriginSalesDTO.class).getResultList();

		LocalDate now = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			LocalDateTime idealMonth = LocalDateTime.parse(String.format("%d/%02d", now.getYear(), now.getMonth().getValue()));

			boolean hasMonth = salesNationality.stream().anyMatch(v -> v.getMonth().equals(idealMonth));
			if (!hasMonth) {
				salesNationality.add(i - 1, new OriginSalesDTO(idealMonth, 0, 0));
			}

			now = now.minusMonths(1);
		}

		return salesNationality;
	}

	private Long total(SaleFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Sale> root = query.from(Sale.class);

		addFilter(filter, builder, root);

		query.select(builder.count(root));
		return manager.createQuery(query).getSingleResult();
	}

	private void addFilter(SaleFilter filter, CriteriaBuilder builder, Root<Sale> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter != null) {
			if (!StringUtils.hasText(String.valueOf(filter.getCode()))) {
				predicates.add(builder.equal(root.get("code"), filter.getCode()));
			}

			if (filter.getStatusSale() != null) {
				predicates.add(builder.equal(root.get("status"), filter.getStatusSale()));
			}

			if (filter.getSince() != null) {
				LocalDateTime since = LocalDateTime.of(filter.getSince(), LocalTime.of(0, 0));
				predicates.add(builder.greaterThanOrEqualTo(root.get("createDate"), since));
			}

			if (filter.getUntil() != null) {
				LocalDateTime until = LocalDateTime.of(filter.getUntil(), LocalTime.of(23, 59));
				predicates.add(builder.lessThanOrEqualTo(root.get("createDate"), until));
			}

			if (filter.getMinimumValue() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("amount"), filter.getMinimumValue()));
			}

			if (filter.getMaximumValue() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("amount"), filter.getMinimumValue()));
			}

			if (!StringUtils.hasText(filter.getCostumerName())) {
				predicates.add(builder.like(builder.lower(root.get("costumer.name")), "%" + filter.getCostumerName().toLowerCase() + "%"));
			}

			if (!StringUtils.hasText(filter.getCpforCnpjCostumer())) {
				predicates.add(builder.equal(root.get("costumer.cpfOrCnpj"), TypePerson.removeFormatting(filter.getCpforCnpjCostumer())));
			}
		}

		builder.and(predicates.toArray(new Predicate[0]));
	}
}
