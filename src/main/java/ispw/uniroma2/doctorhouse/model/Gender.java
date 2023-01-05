package ispw.uniroma2.doctorhouse.model;

import java.util.Optional;
public enum Gender {
    NotKnown(0),
    Female(2),
    Male(1),
    NotApplicable(9);
    Gender(int isoCode) {
        this.isoCode = isoCode;
    }
    public static Optional<Gender> from(int isoCode) {
        switch (isoCode) {
            case 0:
                return Optional.of(NotKnown);
            case 1:
                return Optional.of(Male);
            case 2:
                return Optional.of(Female);
            case 9:
                return Optional.of(NotApplicable);
            default:
                return Optional.empty();
        }
    }
    public final int isoCode;
}
