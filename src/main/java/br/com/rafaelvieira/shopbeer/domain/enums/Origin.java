package br.com.rafaelvieira.shopbeer.domain.enums;

public enum Origin {

	NATIONAL("National"),
	INTERNATIONAL("International");
	
	private final String description;
	
	Origin(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
