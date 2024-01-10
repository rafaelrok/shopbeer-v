package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.Countries;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "city")
public class City implements Serializable {

	@Serial
	private static final long serialVersionUID = 2405172041950251807L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;
	
	@NotBlank(message = "Name is required")
	private String name;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Country is required")
	private Countries country;
	
	@NotNull(message = "State is required")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "code_state")
	@JsonIgnore
	@ToString.Exclude
	private State state;

	public boolean isNew() {
		return code == null;
	}

	public City(Long code, String name, State state) {
		this.code = code;
		this.name = name;
		this.state = state;
	}

	public boolean hasState() {
		return state != null;
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
		City other = (City) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
