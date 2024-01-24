package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.query.userEmployee.UserEmployeesQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmployeeRepository extends JpaRepository<UserEmployee, Long>, JpaSpecificationExecutor<UserEmployee>, UserEmployeesQuery {

    UserEmployee findByUsername(String username);

    Optional<UserEmployee> findByUsernameIgnoreCase(String username);

    Optional<UserEmployee> findByEmail(String email);

    List<UserEmployee> findByCodeIn(Collection<Long> code);
}
