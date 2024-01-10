package br.com.rafaelvieira.shopbeer.repository.pagination;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * @update Use {@link #prepare(CriteriaBuilder, Pageable)} instead.
     */
    public void prepare(CriteriaBuilder criteriaBuilder, Pageable pageable) {

        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root root = criteriaQuery.from(Object.class);

        criteriaQuery.select(root);

        int currentPage = pageable.getPageNumber();
        int totalRecordsPerPage = pageable.getPageSize();
        int firstRegistration = currentPage * totalRecordsPerPage;

        Sort sort = pageable.getSort();
        if (sort != null && sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            Order jpaOrder = order.isAscending()
                    ? criteriaBuilder.asc(root.get(property))
                    : criteriaBuilder.desc(root.get(property));
            criteriaQuery.orderBy(jpaOrder);
        }

        TypedQuery<Object> query = entityManager.createQuery(criteriaQuery);

        query.setFirstResult(firstRegistration);
        query.setMaxResults(totalRecordsPerPage);
    }

    /**
     * @version  Use {@link #prepare(CriteriaBuilder, Pageable)} instead.
     */
//    public <T> CriteriaQuery<T> prepare(Class<T> type, Pageable pageable) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
//        Root<T> root = criteriaQuery.from(type);
//
//        int paginaAtual = pageable.getPageNumber();
//        int totalRegistrosPorPagina = pageable.getPageSize();
//        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
//
//        criteriaQuery.select(root);
//
//        Sort sort = pageable.getSort();
//        if (sort != null && sort.isSorted()) {
//            Sort.Order order = sort.iterator().next();
//            String property = order.getProperty();
//            Order jpaOrder = order.isAscending() ? criteriaBuilder.asc(root.get(property)) : criteriaBuilder.desc(root.get(property));
//            criteriaQuery.orderBy(jpaOrder);
//        }
//
//        entityManager.createQuery(criteriaQuery)
//                .setFirstResult(primeiroRegistro)
//                .setMaxResults(totalRegistrosPorPagina);
//
//        return criteriaQuery;
//    }
}
