package gusgo.sg_interface.application.resources;

public enum YesNo {

    YES("Sim", "YES"),
    NO("Não", "NO");

    private final String name;

    private final String value;

    YesNo(String name, String value) {
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
