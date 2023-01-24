package ispw.uniroma2.doctorhouse.beans;

public enum GenderBean {
    NOT_KNOWN(0),
    MALE(1),
    FEMALE(2),
    NOT_APPLICABLE(9);

    GenderBean(int isoCode) {
        this.isoCode = isoCode;
    }

    public final int isoCode;
}
