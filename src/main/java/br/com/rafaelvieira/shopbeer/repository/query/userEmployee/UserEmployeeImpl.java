package br.com.rafaelvieira.shopbeer.repository.query.userEmployee;

import br.com.rafaelvieira.shopbeer.domain.GroupEmployee;
import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.domain.UserGroup;
import br.com.rafaelvieira.shopbeer.repository.filter.UserEmployeeFilter;
import br.com.rafaelvieira.shopbeer.repository.pagination.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class UserEmployeeImpl implements UserEmployeesQuery {

	@PersistenceContext
	private EntityManager manager;

	private final PaginationUtil paginationUtil;

	public UserEmployeeImpl(PaginationUtil paginationUtil) {
		this.paginationUtil = paginationUtil;
	}

	@Override
	public Optional<UserEmployee> findByEmail(String email) {
		return manager
				.createQuery("from UserEmployee where lower(email) = lower(:email) and active = true", UserEmployee.class)
				.setParameter("email", email).getResultList().stream().findFirst();
	}

	@Override
	public List<String> findByGroupEmployees(UserEmployee userEmployee) {
		return manager.createQuery(
				"select distinct p.name from UserEmployee u inner join u.groupEmployees g inner join g.permissions p where u = :userEmployee", String.class)
				.setParameter("userEmployee", userEmployee)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<UserEmployee> findByName(UserEmployeeFilter filter, Pageable pageable) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UserEmployee> cq = cb.createQuery(UserEmployee.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		addFilter(filter, cb, cq, root);
		paginationUtil.prepare(cb, pageable);

		// Execute a consulta e obtenha o resultado
		int totalRows = manager.createQuery(cq).getResultList().size();
		cq.select(root);
		List<UserEmployee> list = manager.createQuery(cq)
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize())
				.getResultList();

		return new PageImpl<>(list, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), totalRows);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<UserEmployee> findByCode(Long code) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UserEmployee> cq = cb.createQuery(UserEmployee.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		cq.where(cb.equal(root.get("code"), code));

		return Optional.ofNullable(manager.createQuery(cq).getSingleResult());
	}

	private Long total(UserEmployeeFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<UserEmployee> root = cq.from(UserEmployee.class);

		// Adicione os filtros aqui, por exemplo:
		addFilter(filter, cb, null, root);

		cq.select(cb.count(root));

		return manager.createQuery(cq).getSingleResult();
	}

	private void addFilter(UserEmployeeFilter filter, CriteriaBuilder cb, CriteriaQuery<UserEmployee> cq, Root<UserEmployee> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (filter != null) {
			if (!StringUtils.hasText(filter.getName())) {
				predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
			}

			if (!StringUtils.hasText(filter.getEmail())) {
				predicates.add(cb.like(root.get("email"), filter.getEmail() + "%"));
			}

			if (filter.getGroupEmployees() != null && !filter.getGroupEmployees().isEmpty()) {
				for (Long codigoGrupo : filter.getGroupEmployees().stream().mapToLong(GroupEmployee::getCode).toArray()) {
					Subquery<Long> subquery = cq.subquery(Long.class);
					Root<UserGroup> subRoot = subquery.from(UserGroup.class);
					subquery.select(subRoot.get("id").get("userEmployee"));
					subquery.where(cb.equal(subRoot.get("id").get("group").get("code"), codigoGrupo));
					predicates.add(cb.in(root.get("code")).value(subquery));
				}
			}
		}
		cq.where(predicates.toArray(new Predicate[0]));
	}
}
