package br.com.rafaelvieira.shopbeer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "group_employee")
@AllArgsConstructor
public class GroupEmployee implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "group_permission",
			joinColumns = @JoinColumn(name = "code_group_employee"),
			inverseJoinColumns = @JoinColumn(name = "code_permission"))
	@ToString.Exclude
	private List<Permission> permissions;


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
		GroupEmployee other = (GroupEmployee) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
