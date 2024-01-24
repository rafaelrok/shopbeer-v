package br.com.rafaelvieira.shopbeer.services;

import static org.slf4j.LoggerFactory.getLogger;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusUserEmployee;
import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;
import br.com.rafaelvieira.shopbeer.services.exception.EmailUserAlreadyRegisteredException;
import br.com.rafaelvieira.shopbeer.services.exception.PasswordRequiredUserException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserEmployeeService {

    private static final Logger LOGGER = getLogger(UserEmployeeService.class);
    
    private final UserEmployeeRepository userEmployeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEmployeeService(UserEmployeeRepository userEmployeeRepository, PasswordEncoder passwordEncoder) {
        this.userEmployeeRepository = userEmployeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void insert(UserEmployee userEmployee) {
        Optional<UserEmployee> existingUser = userEmployeeRepository.findByEmail(userEmployee.getEmail());
        if (existingUser.isPresent() && !existingUser.get().equals(userEmployee)) {
            throw new EmailUserAlreadyRegisteredException("E-mail already registered");
        }

        if (userEmployee.isNew() && !StringUtils.hasText(userEmployee.getPassword())) {
            throw new PasswordRequiredUserException("Password is mandatory for new user");
        }

        if (userEmployee.isNew() || !StringUtils.hasText(userEmployee.getPassword())) {
            userEmployee.setPassword(this.passwordEncoder.encode(userEmployee.getPassword()));
        } else if (StringUtils.hasText(userEmployee.getPassword())) {
            userEmployee.setPassword(existingUser.orElseThrow().getPassword());
        }
        userEmployee.setConfirmPassword(userEmployee.getPassword());

        if (!userEmployee.isNew() && userEmployee.getActive() == null) {
            userEmployee.setActive(existingUser.orElseThrow().getActive());
        }

        LOGGER.info("Saving user: {}", userEmployee);
        userEmployeeRepository.save(userEmployee);
    }

    @Transactional
    public void changeStatus(Long[] code, StatusUserEmployee statusUserEmployee) {
        statusUserEmployee.toExecute(code, userEmployeeRepository);
    }
    
    public List<UserEmployee> findAll() {
        return userEmployeeRepository.findAll();
    }

    public UserEmployee findByUsername(String username) {
        return userEmployeeRepository.findByUsername(username);
    }

    public Optional<UserEmployee> findByUsernameIgnoreCase(String username) {
        return userEmployeeRepository.findByUsernameIgnoreCase(username);
    }

    public Optional<UserEmployee> findByEmail(String email) {
        return userEmployeeRepository.findByEmail(email);
    }

    public Optional<UserEmployee> get(Long id) {
        return userEmployeeRepository.findById(id);
    }

    public UserEmployee update(UserEmployee entity) {
        Optional<UserEmployee> existingUser = userEmployeeRepository.findByEmail(entity.getEmail());
        if (existingUser.isPresent() && !existingUser.get().equals(entity)) {
            throw new EmailUserAlreadyRegisteredException("E-mail Já esta sendo utilizado.");
        }
        return userEmployeeRepository.save(entity);
    }

    public void delete(Long id) {
        userEmployeeRepository.deleteById(id);
    }

    public Page<UserEmployee> list(Pageable pageable) {
        return userEmployeeRepository.findAll(pageable);
    }

    public Page<UserEmployee> list(Pageable pageable, Specification<UserEmployee> filter) {
        return userEmployeeRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) userEmployeeRepository.count();
    }
}
