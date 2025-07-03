package gusgo.sg_interface.application.resources;

public enum Status {

    ACTIVE("Ativo", "ACTIVE"),
    INACTIVE("Inativo", "INACTIVE");

    private final String name;

    private final String value;

    Status(String name, String value) {
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
