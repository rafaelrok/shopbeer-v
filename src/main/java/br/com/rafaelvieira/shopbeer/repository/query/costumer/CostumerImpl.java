package br.com.rafaelvieira.shopbeer.repository.query.costumer;

import br.com.rafaelvieira.shopbeer.domain.Address;
import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.filter.CostumerFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Component
public class CostumerImpl implements CostumerQuery {

	@PersistenceContext
	private EntityManager manager;

	private final PaginationUtil PaginationUtil;

	public CostumerImpl(PaginationUtil PaginationUtil) {
		this.PaginationUtil = PaginationUtil;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Costumer> findByName(CostumerFilter costumerFilter, Pageable pageable) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Costumer> cq = cb.createQuery(Costumer.class);
		Root<Costumer> costumer = cq.from(Costumer.class);
		Predicate predicate = cb.conjunction();
		addFilter(costumerFilter, cb, predicate, costumer);
		Join<Costumer, Address> addressJoin = costumer.join("address", JoinType.LEFT);
		Join<Address, City> cityJoin = addressJoin.join("city", JoinType.LEFT);

		predicate = cb.and(predicate, cb.equal(cityJoin.get("state").get("id"), 1));
//		predicate = cb.equal(cityJoin.get("state").get("id"), 1);

		cq.where(predicate);
		TypedQuery<Costumer> query = manager.createQuery(cq);

		// Apply pagination
		PaginationUtil.prepare(cb, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(costumerFilter));
	}

	private Long total(CostumerFilter costumerFilter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Costumer> costumer = cq.from(Costumer.class);
		Predicate predicate = cb.conjunction();
		addFilter(costumerFilter, cb, predicate, costumer);

		cq.select(cb.count(costumer)).where(predicate);
		return manager.createQuery(cq).getSingleResult();
	}

	private void addFilter(CostumerFilter costumerFilter, CriteriaBuilder cb, Predicate predicate, Root<Costumer> costumer) {
		if (costumerFilter != null) {
			if (!StringUtils.hasText(costumerFilter.getName())) {
				predicate = cb.and(predicate, cb.like(cb.lower(costumer.get("name")), "%" + costumerFilter.getName().toLowerCase() + "%"));
			}

			if (!StringUtils.hasText(costumerFilter.getCpfOrCnpj())) {
				cb.and(predicate, cb.equal(costumer.get("cpfOrCnpj"), costumerFilter.getCpfOrCnpjNoFormatting()));
			}
		}
	}
}
