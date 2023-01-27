package ispw.uniroma2.doctorhouse.beans;

public class DrugPrescriptionBean extends VisitPrescriptionBean {
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
