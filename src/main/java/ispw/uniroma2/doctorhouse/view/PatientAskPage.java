package ispw.uniroma2.doctorhouse.view;

import ispw.uniroma2.doctorhouse.navigation.Navigator;
import ispw.uniroma2.doctorhouse.navigation.patient.PatientDestination;
import ispw.uniroma2.doctorhouse.rearrange.AskForRearrange;

public class PatientAskPage extends AskPage<PatientDestination> {
    protected PatientAskPage(AskForRearrange controller, Navigator<PatientDestination> navigator) {
        super(controller, navigator);
    }
}
