package utils;

import java.io.Serializable;
import java.util.Set;

/**
 * La clase Moment representa un instante dado en la semana (minutos que pasan desde las 00:00hs
 * para un día determinado) 
 */
public class Moment implements /*Comparable<Moment>,*/ TimeConstants, Serializable{
	private static final long serialVersionUID = 1L;
	private Day day;

	/** Un intervalo de tiempo que guarda la cantidad de minutos
	 * que transcurrieron desde las 00:00 hs*/
    private Time timeOfDay; 

    /**
     * Construye el momento del día <i>day</i> y horario según los minutos que
     * representa <i>timeOfDay</i>, que debe ser menor a la cantidad de minutos
     * en un dia, menos uno (en efecto, de 00:00 a 23:59).
     * 
     * @throws IllegalArgumentException Si se pasa un Time con mas minutos que los que
     * hay en un dia
     */
    public Moment (Day day, Time timeOfDay) {
        this.day = day;
        if(timeOfDay.getMinutes() > TimeConstants.MINUTES_PER_DAY - 1)//00:00 - 23:59 = 0 - MINUTES_PER_DAY - 1
        	throw new IllegalArgumentException("Time tiene que ser entre 00:00 y 24:00");
        this.timeOfDay = timeOfDay;
    }
    

    /**
     * Devuelve el horario en forma de Time
     * (minutos desde el inicio del dia)
     */
    public Time getTime () {
        return timeOfDay;
    }

    /**
     * El dia de este momento
     */
    public Day getDay () {
        return day;
    }

    /**
     * Devuelve el tiempo que falta para que sea el momento que se pasa como parametro
     */
    public Time howMuchUntil (Moment other) {
        int diff = deltaMinutes(other);

        if (diff < 0) //El momento es "antes en la semana", es decir, calculo para la semana que viene
            diff += MINUTES_PER_WEEK;
        
        return new Time(diff);

    }

    /**
     * Calcula la diferencia en minutos entre un momento y otro, segun
     * su orden en la semana (Lunes antes que Martes, etc.). 
     * 
     * En efecto, la cuenta esta dada segun 
     * <i>(minutos desde el lunes a las 00:00hs hasta el otro momento)<i>
     * - <i>(minutos desde el lunes a las 00hs hasta el momento actual)</i>
     * 
     * Es decir, si el que se pasa como parametro esta antes en la semana,
     * se devuelve un numero negativo
     * 
     * @param other - el otro momento
     */
    private int deltaMinutes (Moment other) {
        int daysDiff = this.day.getDaysDifference(other.day);
        int minuteDiff = other.timeOfDay.getMinutes() - this.timeOfDay.getMinutes();
        return daysDiff * MINUTES_PER_DAY + minuteDiff;

    }

    
    /**
     * Devuelve el momento en la semana que resulta de esperar un tiempo 
     * como el que se pasa como par&aacute;metro a partir de este Moment.
     * @param t - el tiempo a esperar (a&ntilde;adir).
     */
    public Moment addTime (Time t) {
        Day newDay = day;
        int minSum = timeOfDay.getMinutes() + t.getMinutes();
        while (minSum >= MINUTES_PER_DAY) {
            minSum -= MINUTES_PER_DAY;
            newDay = newDay.getNextDay();
        }
        return new Moment(newDay, new Time(minSum));
    }

//    @Override public int compareTo (Moment o) {
//        return deltaMinutes(o);
//    }
//
//    /**
//     * Devuelve si estï¿½ antes en la semana
//     */
//    public boolean isBefore (Moment other) {
//        return compareTo(other) < 0;
//    }
//
//    /**
//     * Devuelve si estï¿½ despuï¿½s en la semana
//     */
//    public boolean isAfter (Moment other) {
//        return compareTo(other) > 0;
//    }
//
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
