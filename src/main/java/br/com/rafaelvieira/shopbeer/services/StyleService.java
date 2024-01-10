package br.com.rafaelvieira.shopbeer.services;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.StyleRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.StyleFilter;
import br.com.rafaelvieira.shopbeer.services.exception.NameStyleAlreadyRegisteredException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StyleService {

    private final StyleRepository styleRepository;


    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Transactional(readOnly = true)
    public Style filtered(StyleFilter filter) {
        return styleRepository.findByName(filter, null).getContent().get(0);

    }

    @Transactional
    public Style save(Style style) {
        Optional<Style> styleOptional = styleRepository.findByNameIgnoreCase(style.getName());
        if (styleOptional.isPresent()) {
            throw new NameStyleAlreadyRegisteredException("Name of the style already registered");
        }

        return styleRepository.saveAndFlush(style);
    }

    public Optional<Style> get(Long id) {
        return styleRepository.findById(id);
    }

    public Style update(Style entity) {
        return styleRepository.save(entity);
    }

    public void delete(Long id) {
        styleRepository.deleteById(id);
    }

    public Page<Style> list(Pageable pageable) {
        return styleRepository.findAll(pageable);
    }

    public Page<Style> list(Pageable pageable, Specification<Style> filter) {
        return styleRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) styleRepository.count();
    }
}