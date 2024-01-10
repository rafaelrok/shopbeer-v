package br.com.rafaelvieira.shopbeer.security;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPermissionChecker<T> {

    private final SecurityContextHolder securityContextHolder;
    private final List<String> allowedRoles;

    public UserPermissionChecker(SecurityContextHolder securityContextHolder,
                                 List<String> allowedRoles) {
        this.securityContextHolder = securityContextHolder;
        this.allowedRoles = allowedRoles;
    }

    /**
     * Verifica se o usuário logado tem uma das roles permitidas.
     *
     * @return true se o usuário logado tem permissão
     * @throws IllegalStateException se não houver um usuário logado
     */
    public boolean checkPermission() {

        Authentication auth = securityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            throw new IllegalStateException("Nenhum usuário autenticado");
        }

        T loggedInUser = (T) auth.getPrincipal();

        if(loggedInUser == null) {
            throw new IllegalStateException("Nenhum usuário logado");
        }

        if(loggedInUser instanceof UserDetails user) {
            for (GrantedAuthority role : user.getAuthorities()) {
                    if (allowedRoles.contains(role.getAuthority())) {
                        return true;
                    }
                }
        }

        return false;
    }

}
