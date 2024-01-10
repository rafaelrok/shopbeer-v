package br.com.rafaelvieira.shopbeer.repository.query.style;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.filter.StyleFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class StylesImpl implements StylesQuery {
	
	@PersistenceContext
	private EntityManager manager;
	
	private final PaginationUtil paginationUtil;

	public StylesImpl(PaginationUtil paginationUtil) {
		this.paginationUtil = paginationUtil;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Style> findByName(StyleFilter filter, Pageable pageable) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Style> cq = cb.createQuery(Style.class);
		Root<Style> style = cq.from(Style.class);

		// Add filters
		addFilter(filter, cb, cq, style);

		// Add ordering and pagination
		cq.orderBy(QueryUtils.toOrders(pageable.getSort(), style, cb));
		TypedQuery<Style> query = manager.createQuery(cq);
		paginationUtil.prepare(cb, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filter));
	}

	private Long total(StyleFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Style> style = cq.from(Style.class);

		// Add filters
		addFilter(filter, cb, cq, style);

		cq.select(cb.count(style));
		return manager.createQuery(cq).getSingleResult();
	}

	private void addFilter(StyleFilter filter, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<Style> style) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter != null && (!StringUtils.hasText(filter.getName()))) {
				predicates.add(cb.like(cb.lower(style.get("name")), "%" + filter.getName().toLowerCase() + "%"));

		}

		cq.where(predicates.toArray(new Predicate[0]));
	}

}
