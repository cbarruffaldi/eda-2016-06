package utils;

public class Moment implements Comparable<Moment> {
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int DAYS_IN_A_WEEK = 7;
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    
    private Day day;
    private Time timeOfDay; // Desde las 00:00 hs

    // timeOfDay es el tiempo pasado desde las 00:00 hs de el Day dado.
    public Moment(Day day, Time timeOfDay) {
        this.day = day;
        this.timeOfDay = timeOfDay;
    }

    /**Devuelve el tiempo que falta para que sea el día que se pasa como parámetro*/
    public Time howMuchUntil(Moment other) {
    	int diff = deltaMinutes(other);  
    
        if(diff < 0) //El momento es "antes en la semana", es decir, calculo para la semana que viene
        	diff += DAYS_IN_A_WEEK*MINUTES_PER_DAY;
        
        return new Time(diff);
        
    }
    
    // (minutos desde el lunes hasta other) - (minutos desde el lunes hasta hoy)
    // es decir, si other esta "antes" en la semana, resulta negativo
    private int deltaMinutes(Moment other){
        int daysDiff = this.day.getDaysDifference(other.day);
        int minuteDiff = other.timeOfDay.getMinutes() - this.timeOfDay.getMinutes();
        return daysDiff*MINUTES_PER_DAY + minuteDiff;

    }
    
    //Necesario?
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
	public int compareTo(Moment o) {
		return deltaMinutes(o);
	}

	/**Devuelve si está antes en la semana*/
	public boolean isBefore(Moment other){
		return compareTo(other) < 0;
	}
	
	/**Devuelve si está después en la semana*/
	public boolean isAfter(Moment other){
		return compareTo(other) > 0;
	}
	
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment other = (Moment) o;

        // Creo que no tiene sentido protegerse de las NPE
        
//        if (day != null ? !day.equals(other.day) : other.day != null) return false;
//        return !(timeOfDay != null ? !timeOfDay.equals(other.timeOfDay) : other.timeOfDay != null);
        
        return deltaMinutes(other) == 0;

    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (timeOfDay != null ? timeOfDay.hashCode() : 0);
        return result;
    }
    
    
    public static void main(String[] args) {
		Moment m1 = new Moment(Day.DO, new Time(12, 00));
		Moment m2 = new Moment(Day.DO, new Time(12, 00));
		
		System.err.println(m1.equals(m2));
	
		Moment m4 = new Moment(Day.LU, new Time(12,00));
		Moment m5 = new Moment(Day.LU, new Time(12,30));
		Moment m6 = new Moment(Day.MI, new Time(15,00));
		Moment m7 = new Moment(Day.JU, new Time(12,00));
		Moment m8 = new Moment(Day.VI, new Time(12,00));

		System.out.println(m1.howMuchUntil(m2));
		System.out.println(m1.howMuchUntil(m5));
		System.out.println(m4.howMuchUntil(m5));

    }

}
 