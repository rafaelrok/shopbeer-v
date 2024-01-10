package br.com.rafaelvieira.shopbeer.domain.enums;

import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;

import java.util.List;

public enum StatusUserEmployee {

    ACTIVATE("ATIVO") {
        @Override
        public void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository) {
            userEmployeeRepository.findByCodeIn(List.of(code)).forEach(u -> u.setActive(true));
        }
    },

DISABLE("INATIVO") {
        @Override
        public void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository) {
            userEmployeeRepository.findByCodeIn(List.of(code)).forEach(u -> u.setActive(false));
        }
    };

    private final String name;

    StatusUserEmployee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository);
}
