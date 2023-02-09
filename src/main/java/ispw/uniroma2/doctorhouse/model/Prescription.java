package ispw.uniroma2.doctorhouse.model;

public class Prescription {
    private final String kind;
    private final String name;
    private final int quantity;

    public Prescription(String kind, String name, int quantity) {
        this.kind = kind;
        this.name = name;
        this.quantity = quantity;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
