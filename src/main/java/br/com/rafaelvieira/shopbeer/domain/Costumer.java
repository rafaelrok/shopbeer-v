package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import br.com.rafaelvieira.shopbeer.domain.validation.CostumerGroupSequenceProvider;
import br.com.rafaelvieira.shopbeer.domain.validation.group.CnpjGroup;
import br.com.rafaelvieira.shopbeer.domain.validation.group.CpfGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "costumer")
@GroupSequenceProvider(CostumerGroupSequenceProvider.class)
public class Costumer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Type person is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "type_person")
	private TypePerson typePerson;

	@NotBlank(message = "CPF/CNPJ is required")
	@CPF(groups = CpfGroup.class)
	@CNPJ(groups = CnpjGroup.class)
	@Column(name = "cpf_cnpj")
	private String cpfcnpj;

	private String telephone;

	@Email(message = "E-mail invalid")
	@Column(unique = true)
	private String email;

	@JsonIgnore
	@Embedded
	private Address address;
	
	@PrePersist @PreUpdate
	private void prePersistPreUpdate() {
		this.cpfcnpj = TypePerson.removeFormatting(this.cpfcnpj);
	}
	
	@PostLoad
	private void postLoad() {
		this.cpfcnpj = this.typePerson.format(this.cpfcnpj);
	}
	
	public String getCpfOrCnpjNoFormatting() {
		return TypePerson.removeFormatting(this.cpfcnpj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Costumer other = (Costumer) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
