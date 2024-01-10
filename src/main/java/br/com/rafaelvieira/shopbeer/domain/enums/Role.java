package br.com.rafaelvieira.shopbeer.domain.enums;

public enum Role {
    USER("USUÁRIO"),
    ADMIN("ADMINISTRADOR");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
