package br.com.rafaelvieira.shopbeer.services;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.CostumerRepository;
import br.com.rafaelvieira.shopbeer.services.exception.CpfCnpjCustomerAlreadyRegisteredException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;

    public CostumerService(CostumerRepository costumerRepository) {
        this.costumerRepository = costumerRepository;
    }

    @Transactional
    public void save(Costumer costumer) {
        Optional<Costumer> existingCostumer = costumerRepository.findByCpfcnpj(costumer.getCpfOrCnpjNoFormatting());
        if (existingCostumer.isPresent()) {
            throw new CpfCnpjCustomerAlreadyRegisteredException("CPF/CNPJ already registered");
        }

        costumerRepository.save(costumer);
    }

    public Optional<Costumer> get(Long id) {
        return costumerRepository.findById(id);
    }

    public Costumer update(Costumer entity) {
        return costumerRepository.save(entity);
    }

    public void delete(Long id) {
        costumerRepository.deleteById(id);
    }

    public Page<Costumer> list(Pageable pageable) {
        return costumerRepository.findAll(pageable);
    }

    public Page<Costumer> list(Pageable pageable, Specification<Costumer> filter) {
        return costumerRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) costumerRepository.count();
    }
}
