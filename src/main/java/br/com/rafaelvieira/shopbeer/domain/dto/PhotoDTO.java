package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

@Data
public class PhotoDTO {

	private String name;
	private String contentType;
	private String url;

	public PhotoDTO(String name, String contentType, String url) {
		this.name = name;
		this.contentType = contentType;
		this.url = url;
	}

	public PhotoDTO() {

	}
}
