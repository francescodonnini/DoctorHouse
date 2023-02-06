package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.DoctorApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.PatientApplicationControllersFactory;

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


}
