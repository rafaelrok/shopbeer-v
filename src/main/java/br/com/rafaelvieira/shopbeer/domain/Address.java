package br.com.rafaelvieira.shopbeer.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Embeddable
public class Address implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String street;
	
	private String number;
	
	private String complement;
	
	private String zipCode;
	
	@ManyToOne
	@JoinColumn(name = "code_city")
	private City city;
	
	@Transient
	private State state;

	
	public String getNameCityAcronymState() {
		if (this.city != null) {
			return this.city.getName() + "/" + this.city.getState().getAcronym();
		}
		
		return null;
	}
	
}
