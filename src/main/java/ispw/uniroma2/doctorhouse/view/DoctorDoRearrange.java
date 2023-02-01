package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.doctor.DoctorDestination;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import javafx.event.ActionEvent;

public class DoctorDoRearrange extends DoRearrangePage<DoctorDestination> {
    public DoctorDoRearrange(DoRearrange controller, Navigator<DoctorDestination> navigator) {
        super(controller, navigator);
    }

    @Override
    protected void goBack(ActionEvent ignored) {
        navigator.navigate(DoctorDestination.DOCTOR_HOME_PAGE);
    }
}
