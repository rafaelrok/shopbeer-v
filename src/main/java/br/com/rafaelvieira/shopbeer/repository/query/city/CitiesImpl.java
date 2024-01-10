package  br.com.rafaelvieira.shopbeer.repository.query.city;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.repository.filter.CityFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CitiesImpl implements CitiesQuery {

	@PersistenceContext
	private EntityManager manager;

	private final PaginationUtil paginationUtil;

	public CitiesImpl(PaginationUtil paginationUtil) {
		this.paginationUtil = paginationUtil;
	}

//	@Override
//	@Transactional(readOnly = true)
//	public Page<City> findByName(CityFilter filtro, Pageable pageable) {
//		CriteriaBuilder cb = manager.getCriteriaBuilder();
//		CriteriaQuery<City> cq = cb.createQuery(City.class);
//		Root<City> city = cq.from(City.class);
//
//		// Add filters
//		addFilter(filtro, cb, cq, city);
//
//		// Add ordering and pagination
//		cq.orderBy(QueryUtils.toOrders(pageable.getSort(), city, cb));
//		TypedQuery<City> query = manager.createQuery(cq);
//		paginationUtil.prepare(cb, pageable);
//
//		return new PageImpl<>(query.getResultList(), pageable, total(filtro));
//	}

	private Long total(CityFilter filtro) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<City> city = cq.from(City.class);

		// Add filters
		addFilter(filtro, cb, cq, city);

		cq.select(cb.count(city));
		return manager.createQuery(cq).getSingleResult();
	}

	private void addFilter(CityFilter filtro, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<City> city) {
		List<Predicate> predicates = new ArrayList<>();

		if (filtro != null) {
			if (filtro.getState() != null) {
				predicates.add(cb.equal(city.get("state"), filtro.getState()));
			}

			if (!StringUtils.hasText(filtro.getName())) {
				predicates.add(cb.like(cb.lower(city.get("name")), "%" + filtro.getName().toLowerCase() + "%"));
			}
		}

		cq.where(predicates.toArray(new Predicate[0]));
	}
}
