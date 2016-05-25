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

    public Time getTime() {
    	return timeOfDay;
    }

    public Day getDay() {
    	return day;
    }

    /**Devuelve el tiempo que falta para que sea el d�a que se pasa como par�metro*/
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
        Day newDay = day;
        int minSum = timeOfDay.getMinutes() + t.getMinutes();
        if (minSum >= MINUTES_PER_DAY) {
            minSum -= MINUTES_PER_DAY;
            newDay = newDay.getNextDay();
        }
        return new Moment(newDay, new Time(minSum));
    }

	@Override
	public int compareTo(Moment o) {
		return deltaMinutes(o);
	}

	/**Devuelve si est� antes en la semana*/
	public boolean isBefore(Moment other){
		return compareTo(other) < 0;
	}

	/**Devuelve si est� despu�s en la semana*/
	public boolean isAfter(Moment other){
		return compareTo(other) > 0;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment other = (Moment) o;

        return deltaMinutes(other) == 0;

    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (timeOfDay != null ? timeOfDay.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
    	return day.toString() + " " + timeOfDay.toString();
    }


    public static void main(String[] args) {
		Moment m1 = new Moment(Day.DO, new Time(12, 00));
		Moment m2 = new Moment(Day.DO, new Time(12, 00));

		System.err.println(m1.equals(m2));

		Moment m4 = new Moment(Day.LU, new Time(12,00));
		Moment m5 = new Moment(Day.LU, new Time(12,30));
		Moment m6 = new Moment(Day.MI, new Time(15,00));

		System.out.println(m1.howMuchUntil(m2));
		System.out.println(m5.howMuchUntil(m1));
		System.out.println(m1.howMuchUntil(m5));
		System.out.println(m4.howMuchUntil(m5));

		System.out.println(m4.howMuchUntil(m6));

		System.out.println(m4.addTime(new Time(4, 00)));
		System.out.println(m4);
    }

}
