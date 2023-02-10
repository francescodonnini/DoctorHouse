package ispw.uniroma2.doctorhouse.beans;


public class ResponseBean {
    private int requestId;

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }



}
