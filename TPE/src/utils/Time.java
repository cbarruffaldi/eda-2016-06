package utils;

public class Time implements Comparable<Time>, TimeConstants {

    private int minutes;

    public Time(int hour, int min) {
        minutes = hour * 60 + min;
    }

    public Time(int min) {
        minutes = min;
    }

    public Time getSum(Time t) { return new Time(minutes + t.minutes); }

    public Time addTime(Time t) {
        this.minutes += t.minutes;
        return this;
    }

    public Time addMinutes(int minutes) {
        this.minutes += minutes;
        return this;
    }

    public Time sumADay() {
        this.minutes += HOURS_PER_DAY * MINUTES_PER_HOUR;
        return this;
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
        return minutes / MINUTES_PER_DAY + "d " + minutes%MINUTES_PER_DAY / MINUTES_PER_HOUR + "h " + minutes % MINUTES_PER_HOUR + "m";
    }

    @Override
    public int compareTo(Time o) {
        return this.minutes - o.minutes;
    }
}
