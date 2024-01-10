package br.com.rafaelvieira.shopbeer.repository.query.sale;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.domain.dto.OriginSalesDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.SalesMonthDTO;
import br.com.rafaelvieira.shopbeer.repository.filter.SaleFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SalesQuery {

	Page<Sale> findByUuid(SaleFilter filter, Pageable pageable);

	Sale findByCode(Long code);

	@Query("select sum(amount) from Sale where year(createDate) = :year and status = :status")
	BigDecimal totalValueInYear();

	@Query("select sum(amount) from Sale where month(createDate) = :month and status = :status")
	BigDecimal totalValueInMonth();

	@Query("select sum(amount)/count(*) from Sale where year(createDate) = :year and status = :status")
	BigDecimal averageTicketValueInYear();

	@Query(nativeQuery = true, name = "sale.totalByMonth")
	List<SalesMonthDTO> totalByMonth();

	@Query(nativeQuery = true, name = "sale.totalByOrigin")
	List<OriginSalesDTO> totalByOrigin();
}
