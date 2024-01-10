package br.com.rafaelvieira.shopbeer.repository.query.costumer;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.filter.CostumerFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CostumerQuery {

	Page<Costumer> findByName(CostumerFilter filtro, Pageable pageable);
	
}
