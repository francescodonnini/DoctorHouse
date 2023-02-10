package ispw.uniroma2.doctorhouse.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * Class to manipulate a set of disjointed TimeInterval objects which are all subset of a given TimeInterval object - i.e. range.
 */
public class TimeIntervalSet implements Iterable<TimeInterval> {
    private final TimeInterval range;
    private final Queue<TimeInterval> items;

    /**
     *
     * @param range a TimeInterval object that specifies the boundary of the set. All TimeInterval objects that can be added
     *              in the set must fall between the boundaries of range - i.e. range must contain the object.
     */
    public TimeIntervalSet(TimeInterval range) {
        this.range = range;
        items = new PriorityQueue<>();
    }

    /**
     * Add item to the set if and only if:
     * 1. item is contained in range - i.e. range.contains(item) returns true;
     * 2. item does not overlap with other items previously added in the set.
     * If at least one of the conditions does not hold the item is not added to the set
     * @param item object to be added in the set
     */
    public void add(TimeInterval item) {
        if (range.contains(item)) {
            for (TimeInterval i : items) {
                if (i.overlaps(item)) {
                    return;
                }
            }
            items.add(item);
        }
    }

    /**
     * Create a TimeIntervalSet object that contains all the contiguous time intervals contained in range that are not already present
     * in the set.
     * @return the complementary set of this set such that all the elements are subset of range
     */
    public TimeIntervalSet complementary() {
        TimeIntervalSet set = new TimeIntervalSet(range);
        if (!items.isEmpty()) {
            Queue<TimeInterval> copy = new PriorityQueue<>(items);
            LocalTime i1 = range.getStart();
            TimeInterval item = copy.poll();
            LocalTime i2 = item.getStart();
            createInterval(i1, i2).ifPresent(set::add);
            while (!copy.isEmpty()) {
                i1 = item.getEnd();
                item = copy.poll();
                i2 = item.getStart();
                createInterval(i1, i2).ifPresent(set::add);
            }
            createInterval(item.getEnd(), range.getEnd()).ifPresent(set::add);
        } else {
            set.add(range);
        }
        return set;
    }

    /**
     * utility method that creates a TimeInterval if and only if i1 < i2
     * @param i1 start of the interval
     * @param i2 end of the interval
     * @return an optional containing a time interval if i1 < i2, an empty optional otherwise
     */
    private Optional<TimeInterval> createInterval(LocalTime i1, LocalTime i2) {
        if (!Duration.between(i1, i2).isZero()) {
            return Optional.of(new ClockInterval(i1, i2));
        }
        return Optional.empty();
    }


    @Override
    public Iterator<TimeInterval> iterator() {
        return items.iterator();
    }

    @Override
    public void forEach(Consumer<? super TimeInterval> action) {
        items.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeIntervalSet that = (TimeIntervalSet) o;
        return range.equals(that.range) && items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(range, items);
    }
}
