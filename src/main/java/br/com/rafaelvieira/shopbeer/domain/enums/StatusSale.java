package br.com.rafaelvieira.shopbeer.domain.enums;

public enum StatusSale {

	BUDGET("Budget"),
	ISSUED("Issued"),
	CANCELED("Canceled");

	private final String description;

	StatusSale(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
