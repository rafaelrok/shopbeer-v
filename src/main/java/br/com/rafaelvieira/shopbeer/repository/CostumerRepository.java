package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.query.costumer.CostumerQuery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer, Long>, JpaSpecificationExecutor<Costumer>, CostumerQuery {

    Optional<Costumer> findByCpfcnpj(String cpfcnpj);

    boolean existsByCpfcnpj(String cpfcnpj);

    Optional<Costumer> findByNameStartingWithIgnoreCase(String name);
    
    Optional<Costumer> findByEmail(String email);
    

}
