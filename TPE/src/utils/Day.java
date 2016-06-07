package utils;

import java.util.Iterator;

public class Day {

    public static final Day LU = new Day(0);
    public static final Day MA = new Day(1);
    public static final Day MI = new Day(2);
    public static final Day JU = new Day(3);
    public static final Day VI = new Day(4);
    public static final Day SA = new Day(5);
    public static final Day DO = new Day(6);

    private static final Day days[] = {LU, MA, MI, JU, VI, SA, DO};
    public static final int TOTAL_DAYS = days.length;
    private static final String daysStr[] = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
    private final int daysIndex;

    private Day (int index) {
        daysIndex = index;
    }

    public static Day getDay (String day) {
        for (int i = 0; i < TOTAL_DAYS; i++)
            if (daysStr[i].equals(day))
                return days[i];
        throw new IllegalArgumentException("Not valid day name");
    }

    public static <T> WeekArray<T> newWeekArray () {
        return new WeekArray<T>();
    }

    public Day getNextDay () {
        int i = (daysIndex + 1) % TOTAL_DAYS;
        return days[i];
    }

    public Day previousDay () {
        int i = daysIndex > 0 ? (daysIndex - 1) : TOTAL_DAYS - 1;
        return days[i];
    }

    public int getDaysDifference (Day d1) {
        return d1.daysIndex - this.daysIndex;
    }

    @Override public String toString () {
        return daysStr[daysIndex];
    }

    @Override public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Day day = (Day) o;

        return daysIndex == day.daysIndex;
    }

    @Override public int hashCode () {
        return daysIndex;
    }

    public static class WeekArray <T> implements Iterable<T> {
        @SuppressWarnings("unchecked") private T[] array = (T[]) new Object[TOTAL_DAYS];

        public void insert (Day day, T value) {
            array[day.daysIndex] = value;
        }

        public T get (Day day) {
            return array[day.daysIndex];
        }

        public Iterator<T> iterator () {
            return new WeekIterator<T>(array);
        }

        public Iterator<T> iteratorFrom (Day day) {
            return new WeekIterator<T>(array, day.daysIndex);
        }

    }


    private static class WeekIterator <T> implements Iterator<T> {
        private int dayCount = 0;
        private int current = 0;
        private T[] array;

        public WeekIterator (T[] a) {
            array = a;
        }

        public WeekIterator (T[] a, int from) {
            if (from >= TOTAL_DAYS)
                throw new IllegalArgumentException("Out of bounds");
            array = a;
            current = from;
        }

        @Override public boolean hasNext () {
            return dayCount < TOTAL_DAYS;
        }

        @Override public T next () {
            T elem = array[current % TOTAL_DAYS];
            current++;
            dayCount++;
            return elem;
        }
    }

}
