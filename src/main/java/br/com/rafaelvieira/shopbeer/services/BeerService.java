package br.com.rafaelvieira.shopbeer.services;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.repository.BeerRepository;
import br.com.rafaelvieira.shopbeer.services.exception.ImpossibleDeleteEntityException;
import br.com.rafaelvieira.shopbeer.storage.PhotoStorage;
import jakarta.persistence.PersistenceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BeerService {

    private final BeerRepository beerRepository;
    private final PhotoStorage photoStorage;

    public BeerService(BeerRepository beerRepository, PhotoStorage photoStorage) {
        this.beerRepository = beerRepository;
        this.photoStorage = photoStorage;
    }

    @Transactional
    public void save(Beer beer) {
        beerRepository.save(beer);
    }

    @Transactional
    public void delete(Beer beer) {
        try {
            String photo = beer.getPhoto();
            beerRepository.delete(beer);
            beerRepository.flush();
            photoStorage.delete(photo);
        } catch (PersistenceException e) {
            throw new ImpossibleDeleteEntityException("Impossible to put out beer. It has already been used in some sales.");
        }
    }

    public Optional<Beer> get(Long id) {
        return beerRepository.findById(id);
    }

    public Beer update(Beer entity) {
        return beerRepository.save(entity);
    }

//    public void delete(Long id) {
//        beerRepository.deleteById(id);
//    }

    public Page<Beer> list(Pageable pageable) {
        return beerRepository.findAll(pageable);
    }

    public Page<Beer> list(Pageable pageable, Specification<Beer> filter) {
        return beerRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) beerRepository.count();
    }
}
