package utils;

import java.io.Serializable;

public class Time implements Comparable<Time>, TimeConstants, Serializable {

	private static final long serialVersionUID = 1L;
	private int minutes;

    public Time (int hour, int min) {
        minutes = hour * 60 + min;
    }

    public Time (int min) {
        minutes = min;
    }

    public Time (Double min) {
        this(min.intValue());
    }
    
    public Time(String str) {
    	this(0);
    	if(str.matches("[0-9]+:[0-9]+)")){
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

    public Time addTime (Time t) {
        return new Time(minutes + t.minutes);
    }

    public Time addMinutes (int minutes) {
        return new Time(this.minutes + minutes);
    }

    public Time sumADay () {
        return new Time(minutes + HOURS_PER_DAY * MINUTES_PER_HOUR);
    }

    public int minutesDifference (Time t) {
        return Math.abs(this.minutes - t.minutes);
    }

    public int getMinutes () {
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
        return String.format("%02dh%02dm", hours, mins);  // Formato: xxhyym
    }

    @Override public int compareTo (Time o) {
        return this.minutes - o.minutes;
    }
}
