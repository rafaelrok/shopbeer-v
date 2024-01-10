package br.com.rafaelvieira.shopbeer.repository.query.style;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.filter.StyleFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StylesQuery {
	
	Page<Style> findByName(StyleFilter filter, Pageable pageable);
	
}
