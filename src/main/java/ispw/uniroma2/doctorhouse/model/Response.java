package ispw.uniroma2.doctorhouse.model;

public class Response {
    private final String message;
    private final Prescription prescription;
    private int requestId;

    public Response(String message, Prescription prescription, int requestId) {
        this.message = message;
        this.prescription = prescription;
        this.requestId = requestId;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public String getMessage() {
        return message;
    }

    public int getRequestId() {
        return requestId;
    }
}
