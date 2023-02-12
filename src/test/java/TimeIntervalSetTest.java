import ispw.uniroma2.doctorhouse.model.ClockInterval;
import ispw.uniroma2.doctorhouse.model.TimeInterval;
import ispw.uniroma2.doctorhouse.model.TimeIntervalSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TimeIntervalSetTest {
    TimeIntervalSet set;
    @BeforeEach
    void setup() {
        set = new TimeIntervalSet(makeInterval("08:00", "16:00"));
        set.add(makeInterval("09:00", "10:00"));
        set.add(makeInterval("11:23", "12:08"));
        set.add(makeInterval("13:05", "15:01"));
    }

    private TimeInterval makeInterval(String startTime, String endTime) {
        return new ClockInterval(LocalTime.parse(startTime), LocalTime.parse(endTime));
    }

    @Test
    void testComplementaryProperty() {
        TimeIntervalSet complementary = set.complementary();
        Assertions.assertIterableEquals(set, complementary.complementary());
    }
}
