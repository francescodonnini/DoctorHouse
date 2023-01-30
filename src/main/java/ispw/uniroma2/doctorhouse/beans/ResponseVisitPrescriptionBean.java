package ispw.uniroma2.doctorhouse.beans;

public class ResponseVisitPrescriptionBean {
    private int requestId;
    private VisitPrescriptionBean prescriptionBean;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public VisitPrescriptionBean getPrescriptionBean() {
        return prescriptionBean;
    }

    public void setPrescriptionBean(VisitPrescriptionBean prescriptionBean) {
        this.prescriptionBean = prescriptionBean;
    }
}
