package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.DoctorApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.PatientApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.CreateDrugPrescriptionState;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.CreateVisitPrescriptionState;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.ResponsePrescriptionState;

public class StateFactory {
    private final PatientApplicationControllersFactory patientApplicationControllersFactory;
    private final DoctorApplicationControllersFactory doctorApplicationControllersFactory;


    public StateFactory(PatientApplicationControllersFactory patientApplicationControllersFactory, DoctorApplicationControllersFactory doctorApplicationControllersFactory) {
        this.patientApplicationControllersFactory = patientApplicationControllersFactory;
        this.doctorApplicationControllersFactory = doctorApplicationControllersFactory;
    }

    public RequestPrescriptionState createRequestPrescriptionState() {
        return new RequestPrescriptionState(patientApplicationControllersFactory.createRequestPrescription(), this);
    }

    public UserHomePageState createUserHomePageState() {
        return new UserHomePageState(this);
    }

    public DoctorHomePageState createDoctorHomePageState() {
        return new DoctorHomePageState(this);
    }

    public ResponsePrescriptionState createResponsePrescriptionState() {
        return new ResponsePrescriptionState(doctorApplicationControllersFactory.createResponseRequest(), this);
    }

    public CreateDrugPrescriptionState drugPrescriptionState(ResponseRequest responseRequest) {
        return new CreateDrugPrescriptionState(responseRequest, this);
    }

    public CreateVisitPrescriptionState visitPrescriptionState(ResponseRequest responseRequest) {
        return new CreateVisitPrescriptionState(responseRequest, this);
    }


}
