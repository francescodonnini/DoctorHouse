package ispw.uniroma2.doctorhouse.beans;

public class ResponseBean {
    private int requestId;
    VisitPrescriptionBean bean;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public VisitPrescriptionBean getBean() {
        return bean;
    }

    public void setBean(VisitPrescriptionBean bean) {
        this.bean = bean;
    }
}
