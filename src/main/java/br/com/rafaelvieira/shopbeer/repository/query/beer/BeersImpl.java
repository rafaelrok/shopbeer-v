package br.com.rafaelvieira.shopbeer.repository.query.beer;

import java.util.ArrayList;
import java.util.List;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.StockItemValue;
import br.com.rafaelvieira.shopbeer.repository.filter.BeerFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import br.com.rafaelvieira.shopbeer.repository.query.beer.BeersQuery;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class BeersImpl implements BeersQuery {

	@PersistenceContext
	private EntityManager manager;
	
	private final PaginationUtil paginationUtil;
	private final PhotoStorage photoStorage;

	public BeersImpl(PaginationUtil paginationUtil, PhotoStorage photoStorage) {
		this.paginationUtil = paginationUtil;
		this.photoStorage = photoStorage;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Beer> findByName(BeerFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Beer> query = builder.createQuery(Beer.class);
		Root<Beer> root = query.from(Beer.class);
		addFilter(filter, builder, root);

		TypedQuery<Beer> typedQuery = manager.createQuery(query);
		paginationUtil.prepare(builder, pageable);

		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filter));
	}

	@Override
	public List<BeerDTO> findBySku(String skuOrName) {
		String query = "select new br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO(code, sku, name, origin, value, photo) "
				+ "from Beer where lower(sku) like lower(:skuOrName) or lower(name) like lower(:skuOrName)";
		List<BeerDTO> beerFiltered = manager.createQuery(query, BeerDTO.class)
					.setParameter("skuOrName", skuOrName + "%")
					.getResultList();
		beerFiltered.forEach(c -> c.setUrlThumbnailPhoto(photoStorage.getUrl(PhotoStorage.THUMBNAIL_PREFIX + c.getPhoto())));
		return beerFiltered;
	}

	@Override
	public StockItemValue calculateQuantityStock() {
		String query = "select new br.com.rafaelvieira.shopbeer.domain.dto.StockItemValue(sum(value * quantityStock), sum(quantityStock)) from Beer";
		return manager.createQuery(query, StockItemValue.class).getSingleResult();
	}

	private Long total(BeerFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Beer> root = query.from(Beer.class);

		addFilter(filter, builder, root);

		query.select(builder.count(root));
		return manager.createQuery(query).getSingleResult();
	}

	private void addFilter(BeerFilter filter, CriteriaBuilder builder, Root<Beer> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (filter != null) {
			if (!StringUtils.hasText(filter.getSku())) {
				predicates.add(builder.equal(root.get("sku"), filter.getSku()));
			}

			if (!StringUtils.hasText(filter.getName())) {
				predicates.add(builder.like(builder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
			}

			if (isStylePresent(filter)) {
				predicates.add(builder.equal(root.get("style"), filter.getStyle().getCode()));
			}

			if (filter.getFlavor() != null) {
				predicates.add(builder.equal(root.get("flavor"), filter.getFlavor()));
			}

			if (filter.getOrigin() != null) {
				predicates.add(builder.equal(root.get("origin"), filter.getOrigin()));
			}

			if (filter.getValueOf() != null) {
				predicates.add(builder.ge(root.get("value"), filter.getValueOf()));
			}

			if (filter.getValueUpTo() != null) {
				predicates.add(builder.le(root.get("value"), filter.getValueUpTo()));
			}
		}
	}

	private boolean isStylePresent(BeerFilter filter) {
		return filter.getStyle() != null && filter.getStyle().getCode() != null;
	}

}
