package utils;

public class Time implements Comparable {
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;

    private int minutes;

    public Time(int hour, int min) {
        minutes = hour * 60 + min;
    }

    public Time(int min) {
        minutes = min;
    }

    public void sumTime(Time t) {
        this.minutes += t.minutes;
    }

    public void sumADay() {
        this.minutes += HOURS_PER_DAY * MINUTES_PER_HOUR;
    }

    public int minutesDifference(Time t) {
        return Math.abs(this.minutes - t.minutes);
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        return minutes == time.minutes;
    }

    @Override
    public int hashCode() {
        return minutes;
    }

    @Override
    public String toString() {
        return minutes / MINUTES_PER_HOUR + "h " + minutes % MINUTES_PER_HOUR + "m";
    }

    @Override
    public int compareTo(Object o) {
        return this.minutes - ((Time) o).minutes;
    }
}
