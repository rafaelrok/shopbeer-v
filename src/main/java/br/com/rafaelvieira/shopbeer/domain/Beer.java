package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.Flavor;
import br.com.rafaelvieira.shopbeer.domain.enums.Origin;
import br.com.rafaelvieira.shopbeer.validation.SKU;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
//@EntityListeners(BeerEntityListener.class)
@Entity
@Table(name = "beer")
public class Beer implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@SKU
	@NotBlank
	private String sku;

	@NotBlank
	private String name;

	@NotBlank(message = "Description is mandatory")
	@Size(max = 50, message = "Description size must be between 1 and 50")
	private String description;

	@NotNull(message = "Value is mandatory")
	@DecimalMin(value = "0.50", message = "The price of the beer must be greater than R$0.50")
	@DecimalMax(value = "9999999.99", message = "The price of the beer must be less than R$9,999,999.99")
	private BigDecimal value;

	@NotNull(message = "alcoholic content is mandatory")
	@DecimalMax(value = "100.0", message = "The alcohol content value must be less than 100")
	@Column(name = "alcoholic_content")
	private BigDecimal alcoholicContent;

	@NotNull(message = "Commission is mandatory")
	@DecimalMax(value = "100.0", message = "The commission must be equal to or less than 100")
	private BigDecimal commission;

	@NotNull(message = "Quantity in stock is mandatory")
	@Max(value = 9999, message = "The quantity in stock must be less than 9.999")
	@Column(name = "quantity_stock")
	private Integer quantityStock;

	@NotNull(message = "Origin is mandatory")
	@Enumerated(EnumType.STRING)
	private Origin origin;

	@NotNull(message = "Flavor is mandatory")
	@Enumerated(EnumType.STRING)
	private Flavor flavor;

	@NotNull(message = "Style is mandatory")
	@ManyToOne
	@JoinColumn(name = "code_style")
	private Style style;

	private String photo;

	@Column(name = "content_type")
	private String contentType;

	@Transient
	private boolean newPhoto;

	@Transient
	private String urlPhoto;

	@Transient
	private String urlThumbnailPhoto;

	@NotNull(message = "Date of manufacture is mandatory")
	@Column(name = "date_of_manufacture")
	private LocalDate dateOfManufacture;

	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		sku = sku.toUpperCase();
	}

	public String getPhotoOrMock() {
		return !StringUtils.isEmpty(photo) ? photo : "beer-mock.png";
	}

	public boolean withPhoto() {
		return !StringUtils.isEmpty(this.photo);
	}

	public boolean isNew() {
		return code == null;
	}

	public boolean isNewPhoto() {
		return newPhoto;
	}

	public void setNewPhoto(boolean newPhoto) {
		this.newPhoto = newPhoto;
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
		Beer other = (Beer) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
