package ispw.uniroma2.doctorhouse.secondinterface;

import ispw.uniroma2.doctorhouse.DoctorApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.PatientApplicationControllersFactory;
import ispw.uniroma2.doctorhouse.auth.LoginFactory;
import ispw.uniroma2.doctorhouse.beans.AppointmentBean;
import ispw.uniroma2.doctorhouse.beans.DoctorBean;
import ispw.uniroma2.doctorhouse.beans.UserBean;
import ispw.uniroma2.doctorhouse.requestprescription.ResponseRequest;
import ispw.uniroma2.doctorhouse.secondinterface.rearrange.AskState;
import ispw.uniroma2.doctorhouse.secondinterface.rearrange.DoRearrangeState;
import ispw.uniroma2.doctorhouse.secondinterface.rearrange.SelectTimeState;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.CreateDrugPrescriptionState;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.CreateVisitPrescriptionState;
import ispw.uniroma2.doctorhouse.secondinterface.responserequest.ResponsePrescriptionState;

import java.time.LocalDate;

public class StateFactory {
    private final LoginFactory loginFactory;
    private final PatientApplicationControllersFactory patientApplicationControllersFactory;
    private final DoctorApplicationControllersFactory doctorApplicationControllersFactory;


    public StateFactory(LoginFactory loginFactory, PatientApplicationControllersFactory patientApplicationControllersFactory, DoctorApplicationControllersFactory doctorApplicationControllersFactory) {
        this.loginFactory = loginFactory;
        this.patientApplicationControllersFactory = patientApplicationControllersFactory;
        this.doctorApplicationControllersFactory = doctorApplicationControllersFactory;
    }

    public RequestPrescriptionState createRequestPrescriptionState(UserBean loggedUser) {
        return new RequestPrescriptionState(patientApplicationControllersFactory.createRequestPrescription(), this, loggedUser);
    }

    public UserHomePageState createUserHomePageState(UserBean loggedUser) {
        return new UserHomePageState(this, loggedUser);
    }

    public DoctorHomePageState createDoctorHomePageState(UserBean loggedUser) {
        return new DoctorHomePageState(this, loggedUser);
    }

    public ResponsePrescriptionState createResponsePrescriptionState(UserBean loggedUser) {
        return new ResponsePrescriptionState(doctorApplicationControllersFactory.createResponseRequest(), this, loggedUser);
    }

    public CreateDrugPrescriptionState drugPrescriptionState(ResponseRequest responseRequest, UserBean loggedUser) {
        return new CreateDrugPrescriptionState(responseRequest, this, loggedUser);
    }

    public CreateVisitPrescriptionState visitPrescriptionState(ResponseRequest responseRequest, UserBean loggedUser) {
        return new CreateVisitPrescriptionState(responseRequest, this, loggedUser);
    }

    public State createAskState(UserBean loggedUser) {
        if (loggedUser instanceof DoctorBean) {
            return new AskState(this, doctorApplicationControllersFactory.createAskForRearrange(), loggedUser);
        } else {
            return new AskState(this, patientApplicationControllersFactory.createAskForRearrange(), loggedUser);
        }
    }

    public State createDoRearrange(UserBean loggedUser) {
        if (loggedUser instanceof DoctorBean) {
            return new DoRearrangeState(this, doctorApplicationControllersFactory.createDoRearrange(), loggedUser);
        } else {
            return new DoRearrangeState(this, patientApplicationControllersFactory.createDoRearrange(), loggedUser);
        }
    }

    public State createSelectDateState(AppointmentBean bean, LocalDate from, LocalDate to, UserBean loggedUser) {
        if (loggedUser instanceof DoctorBean) {
            return new SelectTimeState(this, doctorApplicationControllersFactory.createAskForRearrange(), from, to, loggedUser, bean);
        } else {
            return new SelectTimeState(this, patientApplicationControllersFactory.createAskForRearrange(), from, to, loggedUser, bean);
        }
    }

    public State createLoginState() {
        return new SecondLoginInterface(loginFactory.create(), this);
    }


}
