package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.query.costumer.CostumerQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer, Long>, JpaSpecificationExecutor<Costumer>, CostumerQuery {

    Optional<Costumer> findByCpfcnpj(String cpfcnpj);

    List<Costumer> findByNameStartingWithIgnoreCase(String name);

}
