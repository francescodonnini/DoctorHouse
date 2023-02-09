package ispw.uniroma2.doctorhouse.model;

public class Request {
    private final int id;
    private final String patientEmail;
    private final String message;

    public Request(int id, String patientEmail, String message) {
        this.id = id;
        this.patientEmail = patientEmail;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getMessage() {
        return message;
    }
}
