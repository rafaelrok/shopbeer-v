package br.com.rafaelvieira.shopbeer.domain.enums;

public enum Flavor {

	SWEET("Sweet"),
	BITTER("Bitter"),
	STRONG("Strong"),
	FRUITTY("Fruity"),
	SMOOTH("Smooth");
	
	private final String description;
	
	Flavor(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
