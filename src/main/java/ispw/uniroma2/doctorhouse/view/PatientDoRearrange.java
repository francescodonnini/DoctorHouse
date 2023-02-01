package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.rearrange.DoRearrange;
import javafx.event.ActionEvent;

public class PatientDoRearrange extends DoRearrangePage<PatientDestination> {
    public PatientDoRearrange(DoRearrange controller, Navigator<PatientDestination> navigator) {
        super(controller, navigator);
    }

    @Override
    protected void goBack(ActionEvent ignored) {
        navigator.navigate(PatientDestination.HOME_PAGE);
    }
}
