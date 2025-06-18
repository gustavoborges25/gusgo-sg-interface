package gusgo.sg_interface.application.resources;

public enum PersonType {

    INDIVIDUAL("Física", "INDIVIDUAL"),
    BUSINESS("Jurídica", "BUSINESS");

    private final String name;

    private final String value;

    PersonType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
