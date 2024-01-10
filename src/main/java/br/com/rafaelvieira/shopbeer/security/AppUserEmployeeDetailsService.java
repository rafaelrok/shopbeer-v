package br.com.rafaelvieira.shopbeer.security;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserEmployeeDetailsService implements UserDetailsService {

    private final UserEmployeeRepository userEmployeeRepository;

    public AppUserEmployeeDetailsService(UserEmployeeRepository userEmployeeRepository) {
        this.userEmployeeRepository = userEmployeeRepository;
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<UserEmployee> userEmployeeOptional = userEmployeeRepository.findByEmail(email);
//        UserEmployee userEmployee = userEmployeeOptional.orElseThrow(() -> new UsernameNotFoundException("Incorrect username and/or password"));
//        return new UserEmployeeSystem(userEmployee, getAuthorities(userEmployee));
//    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEmployee userEmployee = userEmployeeRepository.findByUsername(username);
        if (userEmployee == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else if (!userEmployee.getActive()) {
            throw new UsernameNotFoundException("Usuário: " + username + " Não esta ativo, entre em contato com administrator");
        } else {
            List<GrantedAuthority> authorities = getAuthorities(userEmployee);
            return new User(userEmployee.getUsername(), userEmployee.getPassword(), authorities);

//            return new UserEmployeeSystem(userEmployee, getAuthorities(userEmployee));
//            return new User(userEmployee.getUsername(), userEmployee.getPassword(), getAuthorities(userEmployee));

        }
    }

//    private Collection<? extends GrantedAuthority> getAuthorities(UserEmployee userEmployee) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//
//        List<String> permissions = userEmployeeRepository.findByGroupEmployees(userEmployee);
//        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));
//
//        return authorities;
//    }


    private static List<GrantedAuthority> getAuthorities(UserEmployee userEmployee) {
        return userEmployee.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}

