package br.com.rafaelvieira.shopbeer.repository.query.userEmployee;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.filter.UserEmployeeFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEmployeesQuery {

	Optional<UserEmployee> findByEmail(String email);

	List<String> findByGroupEmployees(UserEmployee userEmployee);

	Page<UserEmployee> findByName(UserEmployeeFilter filter, Pageable pageable);

	Optional<UserEmployee> findByCode(Long code);

}
