package br.com.rafaelvieira.shopbeer.repository.filter;
import br.com.rafaelvieira.shopbeer.domain.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityFilter {

	private State state;
	private String name;

	
}
