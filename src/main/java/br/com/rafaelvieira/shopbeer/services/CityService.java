package br.com.rafaelvieira.shopbeer.services;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.repository.CityRepository;
import br.com.rafaelvieira.shopbeer.services.exception.NameCityAlreadyRegisteredException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public void save(City city) {
        Optional<City> existingCity = cityRepository.findByNameAndState(city.getName(), city.getState());
        if (existingCity.isPresent()) {
            throw new NameCityAlreadyRegisteredException("City name already registered");
        }
        cityRepository.save(city);
    }

//    public Page<City> findByName(CityFilter filter, Pageable pageable) {
//        return cityRepository.findByName(filter, pageable);
//    }

    public Optional<City> findByName(String name) {
        return cityRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return cityRepository.existsByName(name);
    }

    public Optional<City> findByAllIgnoreCase() {
        return cityRepository.findByAllIgnoreCase();
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public Optional<City> get(Long id) {
        return cityRepository.findById(id);
    }

    public City update(City entity) {
        return cityRepository.save(entity);
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }

    public Page<City> list(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public Page<City> list(Pageable pageable, Specification<City> filter) {
        return cityRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) cityRepository.count();
    }
}
