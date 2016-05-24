package flightassistant;

import utils.Day;
import utils.Moment;
import utils.Time;

public class Flight {

	private String airline;
	private int number;
	private double price;
	private Moment departure;
	private Time duration;
	private Moment arrival;
	private Airport origin;
	private Airport destination;
	private Day[] days;  // o mapa <Day, boolean>

	public Flight(String airline, int number, double price, Moment departure, Time duration,
			Airport origin, Airport destination, Day[] days) {
		this.airline = airline;
		this.price = price;
		this.departure = departure;
		this.arrival = departure.addTime(duration);
		this.origin = origin;
		this.destination = destination;
		this.days = days;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		// TODO: Hay que ver como combinamos el string de la aerolinea con el
		// numero de vuelo dependiendo de la estructura que vayamos a usar
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Flight other = (Flight) o;

		if (number == other.number)
			return airline == null ? other.airline == null : airline.equals(other.airline);
		return false;
	}

}
