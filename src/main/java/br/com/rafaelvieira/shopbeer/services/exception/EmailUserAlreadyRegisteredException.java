package br.com.rafaelvieira.shopbeer.services.exception;

public class EmailUserAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmailUserAlreadyRegisteredException(String message) {
		super(message);
	}

}
