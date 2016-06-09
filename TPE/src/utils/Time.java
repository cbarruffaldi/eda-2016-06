package utils;

import java.io.Serializable;

/**
 * La clase <i>Time<i> representa un intervalo temporal, con unidad minima 
 * en minutos. Adem&aacute;s de la informaci&oacute;n en minutos, se proveen 
 * m&eacute;todos para construir a partir de (y obtener) la cantidad de horas completas.
 * El objeto es inmutable.
 */
public class Time implements Comparable<Time>, TimeConstants, Serializable {

	private static final long serialVersionUID = 1L;
	private int minutes;

	/**
	 * Construye un intervalo a partir de una cantidad minutos dados
	 */
    public Time (int mins) {
    	if(mins < 0)
    		throw new IllegalArgumentException("El argumento debe ser positivo");
    	
        minutes = mins;
    }

    
	/**
	 * Construye un intervalo a partir de una cantidad de horas y minutos dados
	 */
    public Time (int hours, int mins) {
    	if(mins < 0 || hours < 0)
    		throw new IllegalArgumentException("El argumento debe ser positivo");

        minutes = hours * MINUTES_PER_HOUR + mins;
    }
    
    /**
     * Construye un intervalo temporal parseando un String, que debe tener formato
     * de numero entero positivo o dos numero enteros positivos separados por dos puntos
     * (Debe validarse la expresion regular: \d+(:\d+)?
     */
    public Time(String str) {
    	this(0);
    	if(str.matches("\\d+:\\d+")){
    		String[] timeStr = str.split(":");
    		this.minutes += Integer.parseUnsignedInt(timeStr[0]) * MINUTES_PER_HOUR;
    		this.minutes += Integer.parseUnsignedInt(timeStr[1]);
    	}
    	else if(str.matches("[0-9]+")){
    		this.minutes += Integer.parseInt(str);
    	}
    	else{
    		throw new IllegalArgumentException("Formato: 'min' o 'horas:minutos'");
    	}
    }
  
    /**
     * Devuelve un nuevo <i>Time</i> con un tiempo igual al del objeto actual mas 
     * el que se pasa como parametro
     * @param t
     * @return
     */
    public Time addTime (Time t) {
        return new Time(minutes + t.minutes);
    }

    
    /**
     * Devuelve un nuevo <i>Time</i> con un tiempo igual al del objeto actual mas 
     * la cantidad de minutos que se pasan como parametro
     * @param t
     * @return
     */
    public Time addMinutes (int minutes) {
        return new Time(this.minutes + minutes);
    }
    
    /**
     * Devuelve la diferencia (en minutos) entre esta instancia y el tiempo dado
     * @return
     */
    public int minutesDifference (Time t) {
        return Math.abs(this.minutes - t.minutes);
    }
    
    /**
     * Devuelve un Time que representa la diferencia entre esta instancia y el tiempo dado
     * @return
     */
    public Time timeDifference(Time t){
    	return new Time(minutesDifference(t));
    }
    
    
    /**
     * Devuelve la cantidad de minutos que tiene esta intervalo temporal
     * @return
     */
    public int getMinutes() {
        return minutes;
    }

    @Override public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Time time = (Time) o;

        return minutes == time.minutes;
    }

    @Override public int hashCode () {
        return minutes;
    }

    @Override public String toString () {
        int hours = minutes / MINUTES_PER_HOUR;
        int mins = minutes % MINUTES_PER_HOUR;
        return String.format("%dh%dm", hours, mins);  // Formato: xxhyym
    }

    @Override public int compareTo (Time o) {
        return this.minutes - o.minutes;
    }
}
