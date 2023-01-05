package ispw.uniroma2.doctorhouse.model;

import java.util.Optional;
public enum Gender {
    NOT_KNOWN(0),
    FEMALE(2),
    MALE(1),
    NOT_APPLICABLE(9);
    Gender(int isoCode) {
        this.isoCode = isoCode;
    }
    public static Optional<Gender> from(int isoCode) {
        switch (isoCode) {
            case 0:
                return Optional.of(NOT_KNOWN);
            case 1:
                return Optional.of(MALE);
            case 2:
                return Optional.of(FEMALE);
            case 9:
                return Optional.of(NOT_APPLICABLE);
            default:
                return Optional.empty();
        }
    }
    public final int isoCode;
}
