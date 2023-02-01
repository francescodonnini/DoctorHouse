package ispw.uniroma2.doctorhouse.beans;

import java.time.Duration;

public class SpecialtyBean {
    private String name;
    private Duration duration;
    private DoctorBean doctor;

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
