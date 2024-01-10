package br.com.rafaelvieira.shopbeer.domain.enums;

public enum Countries {
    BRAZIL("Brasil", "BR"),
    UNITED_STATES("United States", "US"),
    CANADA("Canada", "CA"),
    ARGENTINA("Argentina", "AR"),
    CHILE("Chile", "CL"),
    URUGUAY("Uruguay", "UY"),
    PARAGUAY("Paraguay", "PY"),
    BOLIVIA("Bolivia", "BO"),
    PERU("Peru", "PE"),
    ECUADOR("Ecuador", "EC"),
    COLOMBIA("Colombia", "CO"),
    VENEZUELA("Venezuela", "VE"),
    DOMINICAN_REPUBLIC("Dominican Republic", "DO"),
    CUBA("Cuba", "CU"),
    MEXICO("Mexico", "MX");

    private final String name;
    private final String code;

    Countries(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Countries fromCode(String code) {
        for (Countries country : Countries.values()) {
            if (country.getCode().equals(code)) {
                return country;
            }
        }
        return null;
    }

    public static Countries fromName(String name) {
        for (Countries country : Countries.values()) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }
}
