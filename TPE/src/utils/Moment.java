package utils;

import java.io.Serializable;

public class Moment implements Comparable<Moment>, TimeConstants, Serializable{

	private static final long serialVersionUID = 1L;
	private Day day;
    private Time timeOfDay; // Desde las 00:00 hs

    // timeOfDay es el tiempo pasado desde las 00:00 hs de el Day dado.
    public Moment (Day day, Time timeOfDay) {
        this.day = day;
        this.timeOfDay = timeOfDay;
    }

    public Time getTime () {
        return timeOfDay;
    }

    public Day getDay () {
        return day;
    }

    /**
     * Devuelve el tiempo que falta para que sea el momento que se pasa como par�metro
     */
    public Time howMuchUntil (Moment other) {
        int diff = deltaMinutes(other);

        if (diff
            < 0) //El momento es "antes en la semana", es decir, calculo para la semana que viene
            diff += MINUTES_PER_WEEK;

        return new Time(diff);

    }

    // (minutos desde el lunes hasta other) - (minutos desde el lunes hasta hoy)
    // es decir, si other esta "antes" en la semana, resulta negativo
    private int deltaMinutes (Moment other) {
        int daysDiff = this.day.getDaysDifference(other.day);
        int minuteDiff = other.timeOfDay.getMinutes() - this.timeOfDay.getMinutes();
        return daysDiff * MINUTES_PER_DAY + minuteDiff;

    }

    public Moment addTime (Time t) {
        Day newDay = day;
        int minSum = timeOfDay.getMinutes() + t.getMinutes();
        if (minSum >= MINUTES_PER_DAY) {
            minSum -= MINUTES_PER_DAY;
            newDay = newDay.getNextDay();
        }
        return new Moment(newDay, new Time(minSum));
    }

    @Override public int compareTo (Moment o) {
        return deltaMinutes(o);
    }

    /**
     * Devuelve si est� antes en la semana
     */
    public boolean isBefore (Moment other) {
        return compareTo(other) < 0;
    }

    /**
     * Devuelve si est� despu�s en la semana
     */
    public boolean isAfter (Moment other) {
        return compareTo(other) > 0;
    }

    @Override public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Moment other = (Moment) o;

        return deltaMinutes(other) == 0;

    }

    @Override public int hashCode () {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (timeOfDay != null ? timeOfDay.hashCode() : 0);
        return result;
    }

    @Override public String toString () {
        return day.toString() + " " + timeOfDay.toString();
    }

}
