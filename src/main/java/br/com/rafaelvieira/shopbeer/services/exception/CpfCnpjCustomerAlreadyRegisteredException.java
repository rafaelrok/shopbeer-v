package br.com.rafaelvieira.shopbeer.services.exception;

public class CpfCnpjCustomerAlreadyRegisteredException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CpfCnpjCustomerAlreadyRegisteredException(String message) {
		super(message);
	}

}
