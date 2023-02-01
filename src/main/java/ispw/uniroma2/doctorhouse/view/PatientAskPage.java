package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;
import javafx.event.ActionEvent;

public class PatientAskPage extends AskPage<PatientDestination> {
    protected PatientAskPage(AskForRearrange controller, Navigator<PatientDestination> navigator) {
        super(controller, navigator);
    }

    @Override
    protected void goBack(ActionEvent ignored) {
        navigator.navigate(PatientDestination.HOME_PAGE);
    }
}
