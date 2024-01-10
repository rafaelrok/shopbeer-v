package br.com.rafaelvieira.shopbeer.domain.validation;

import java.util.ArrayList;
import java.util.List;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

public class CostumerGroupSequenceProvider implements DefaultGroupSequenceProvider<Costumer> {

	@Override
	public List<Class<?>> getValidationGroups(Costumer costumer) {
		List<Class<?>> grupos = new ArrayList<>();
		grupos.add(Costumer.class);
		
		if (isPersonSelecionada(costumer)) {
			grupos.add(costumer.getTypePerson().getGroup());
		}
		
		return grupos;
	}

	private boolean isPersonSelecionada(Costumer costumer) {
		return costumer != null && costumer.getTypePerson() != null;
	}

}
