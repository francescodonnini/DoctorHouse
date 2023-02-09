package ispw.uniroma2.doctorhouse.model;

public class Response {
    private final String message;
    private final Prescription prescription;

    public Response(String message, Prescription prescription) {
        this.message = message;
        this.prescription = prescription;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public String getMessage() {
        return message;
    }
}
