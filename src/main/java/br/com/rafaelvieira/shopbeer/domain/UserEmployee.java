package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.domain.enums.Role;
import br.com.rafaelvieira.shopbeer.validation.AttributeConfirmation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AttributeConfirmation(attribute = "password", attributeConfirmation = "confirmPassword"
				, message = "Password confirmation does not match")
@Entity
@Table(name = "user_employee")
@DynamicUpdate
@AllArgsConstructor
public class UserEmployee implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@NotBlank(message = "Name is required")
	private String username;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "E-mail is required")
	@Email(message = "E-mail invalid")
	@Column(unique = true)
	private String email;

	private String password;
	
	@Transient
	private String confirmPassword;

	private Boolean active;

	@Size(min = 1, message = "Select at least one group")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_employee_group", joinColumns = @JoinColumn(name = "code_user_employee")
				, inverseJoinColumns = @JoinColumn(name = "code_group_employee"))
	@ToString.Exclude
	private List<GroupEmployee> groupEmployees;

	@Column(name = "birth_date")
	private LocalDate birthDate;

//	@Lob
//	@Transient
	@Column(name = "profile_picture")
	private String profilePicture;

	@Transient
	private boolean newProfilePicture;

	@Transient
	private String urlProfilePicture;

	@Transient
	private String urlThumbnailProfilePicture;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_employee_role",
			joinColumns = @JoinColumn(name = "code_user_employee"))
	private Set<Role> roles;

	public UserEmployee(Long code, String profilePicture, String name, String username, String email, List<GroupEmployee> groupEmployees, Boolean active) {
		this.code = code;
		this.profilePicture = profilePicture;
		this.name = name;
		this.username = username;
		this.email = email;
		this.groupEmployees = groupEmployees;
		this.active = active;
	}

	public UserEmployee(Long code, String profilePicture, String name, String username, String email, String name1, Boolean active) {
		this.code = code;
		this.profilePicture = profilePicture;
		this.name = name;
		this.username = username;
		this.email = email;
		this.active = active;
	}

	@PreUpdate
	private void preUpdate() {
		this.confirmPassword = password;
	}
	
	public boolean isNew() {
		return code == null;
	}

	public boolean isActive() {
		return active != null && active;
	}

	public boolean withProfilePicture() {
		return !StringUtils.isEmpty(this.profilePicture);
	}

	public boolean isNewProfilePicture() {
		return newProfilePicture;
	}

	public void setNewProfilePicture(boolean newProfilePicture) {
		this.newProfilePicture = newProfilePicture;
	}


	public Collection<? extends GrantedAuthority> getAuthorities() {

		return roles.stream()
				.map(role -> "ROLE_" + role.name()) // adiciona prefixo ROLE_
				.map(SimpleGrantedAuthority::new) // converte para GrantedAuthority
				.collect(Collectors.toList());

	}

//	public Collection<GrantedAuthority> getAuthorities() {
//		return roles.stream()
//				.map(role -> new SimpleGrantedAuthority(role.name()))
//				.collect(Collectors.toList());
//	}
	
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
		UserEmployee other = (UserEmployee) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
