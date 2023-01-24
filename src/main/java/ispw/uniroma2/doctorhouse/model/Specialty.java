package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;

public class Specialty {
    private final String name;
    private final Duration duration;

    public Specialty(String name, Duration duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }
}
