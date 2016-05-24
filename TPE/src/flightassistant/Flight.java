package flightassistant;

import utils.Day;
import utils.Moment;
import utils.Time;

public class Flight {

	private FlightId id;
	private double price;
	private Moment departure;
	private Time duration;
	private Moment arrival;
	private Airport origin;
	private Airport destination;
	private Day[] days;  // o mapa <Day, boolean>

	public Flight(String airline, int number, double price, Moment departure, Time duration,
			Airport origin, Airport destination, Day[] days) {
		this.id = new FlightId(airline, number);
		this.price = price;
		this.departure = departure;
		this.arrival = departure.addTime(duration);
		this.origin = origin;
		this.destination = destination;
		this.days = days;
	}
	
	public boolean isCheaperThan(Flight other) {
		return price < other.price;
	}
	
	public boolean isQuickerThan(Flight other) {
		return duration.compareTo(other.duration) < 0;
	}
	
	public Time getDuration() {
		return duration;
	}
	
	public FlightId getId() {
		return id;
	}
	
	public double getPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
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
		return id.equals(other.id);
	}

}
