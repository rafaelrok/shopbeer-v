package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.domain.State;
import br.com.rafaelvieira.shopbeer.repository.query.city.CitiesQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City>, CitiesQuery {
    List<City> findByStateCode(Long codeState);
    Optional<City> findByAllIgnoreCase();
    Optional<City> findByNameAndState(String name, State state);
    boolean existsByName(String name);
    Optional<City> findByName(String name);
}
