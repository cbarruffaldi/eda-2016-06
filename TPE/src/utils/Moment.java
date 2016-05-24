package utils;

public class Moment implements Comparable {
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    private Day day;
    private Time timeOfDay; // Desde las 00:00 hs

    // timeOfDay es el tiempo pasado desde las 00:00 hs de el Day dado.
    public Moment(Day day, Time timeOfDay) {
        this.day = day;
        this.timeOfDay = timeOfDay;
    }

    public int getTimeDifference(Moment other) {
        int daysDiff = this.day.getDaysDifference(other.day);
        int minuteDiff = this.timeOfDay.minutesDifference(other.timeOfDay);
        return (daysDiff * MINUTES_PER_DAY) + minuteDiff;
    }

    public Moment nextDay() {
        return new Moment(day.getNextDay(), timeOfDay);
    }

    public Moment addTime(Time t) {
        Day newDay = Day.getDay(day.toString()); //TODO: obviamente Day tiene que ser clonable o algo de eso
        int minSum = timeOfDay.getMinutes() + t.getMinutes();
        if (minSum > MINUTES_PER_DAY) {
            minSum -= MINUTES_PER_DAY;
            newDay = newDay.getNextDay();
        }
        return new Moment(newDay, new Time(minSum)); // TODO: no estoy seguro de si esta es la manera de hacer este
                                            // tipo de cosas, devolviendo una instancia nueva.
    }

    @Override
    public int compareTo(Object o) {
        return getTimeDifference((Moment) o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment moment = (Moment) o;

        if (day != null ? !day.equals(moment.day) : moment.day != null) return false;
        return !(timeOfDay != null ? !timeOfDay.equals(moment.timeOfDay) : moment.timeOfDay != null);

    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (timeOfDay != null ? timeOfDay.hashCode() : 0);
        return result;
    }
}
