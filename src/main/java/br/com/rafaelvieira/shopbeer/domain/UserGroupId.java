package br.com.rafaelvieira.shopbeer.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class UserGroupId implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "code_user_employee")
	private UserEmployee userEmployee;
	
	@ManyToOne
	@JoinColumn(name = "code_group_employee")
	private GroupEmployee groupEmployee;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupEmployee == null) ? 0 : groupEmployee.hashCode());
		result = prime * result + ((userEmployee == null) ? 0 : userEmployee.hashCode());
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
		UserGroupId other = (UserGroupId) obj;
		if (groupEmployee == null) {
			if (other.groupEmployee != null)
				return false;
		} else if (!groupEmployee.equals(other.groupEmployee))
			return false;
		if (userEmployee == null) {
			if (other.userEmployee != null)
				return false;
		} else if (!userEmployee.equals(other.userEmployee))
			return false;
		return true;
	}
	
}
