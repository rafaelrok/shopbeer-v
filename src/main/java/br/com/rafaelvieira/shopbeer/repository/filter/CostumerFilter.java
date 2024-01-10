package br.com.rafaelvieira.shopbeer.repository.filter;

import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CostumerFilter {

	private String name;
	private String cpfOrCnpj;

	public Object getCpfOrCnpjNoFormatting() {
		return TypePerson.removeFormatting(this.cpfOrCnpj);
	}
}
