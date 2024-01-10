package br.com.rafaelvieira.shopbeer.repository.query.beer;

import java.util.List;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.StockItemValue;
import br.com.rafaelvieira.shopbeer.repository.filter.BeerFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface BeersQuery {

	Page<Beer> findByName(BeerFilter filter, Pageable pageable);
	
	List<BeerDTO> findBySku(String skuOrName);

	@Query("select new br.com.rafaelvieira.shopbeer.domain.dto.StockItemValue(sum(value * quantityStock), sum(quantityStock)) from Beer")
	StockItemValue calculateQuantityStock();
	
}
