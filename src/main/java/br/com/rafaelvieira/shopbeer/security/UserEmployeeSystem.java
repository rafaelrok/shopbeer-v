package br.com.rafaelvieira.shopbeer.security;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public class UserEmployeeSystem extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UserEmployee userEmployee;

    public UserEmployeeSystem(UserEmployee userEmployee, Collection<? extends GrantedAuthority> authorities) {
        super(userEmployee.getEmail(), userEmployee.getPassword(), authorities);
        this.userEmployee = userEmployee;
    }

    public UserEmployee getUserEmployee() {
        return userEmployee;
    }
}
