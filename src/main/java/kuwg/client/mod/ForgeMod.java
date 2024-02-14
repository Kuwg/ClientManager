package kuwg.client;

public class ForgeMod {
    private final String name, version;

    public ForgeMod(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
