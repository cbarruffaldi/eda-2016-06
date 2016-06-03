package flightassistant;

import utils.Moment;
import utils.Time;

import java.io.Serializable;

public class Flight implements Serializable{

	private static final long serialVersionUID = 1L;

	private FlightId id;
	private double price;

	//private Schedule[] schedules;
	private Moment departure;
	private Time duration;

	private Airport origin;
	private Airport destination;

	public Flight(String airline, int number, double price, Moment departure, Time duration,
			Airport origin, Airport destination) {
		this.id = new FlightId(airline, number);
		this.price = price;
		this.duration = duration;
		this.departure = departure;
		this.origin = origin;
		this.destination = destination;
	}

	public Airport getOrigin() {
		return origin;
	}

	public Time getDepartureTime() {
		return departure.getTime();
	}

	public Moment getDeparture() {
		return departure;
	}

	public Moment getArrival() {
		return departure.addTime(getDuration());
	}

	public Airport getDestination() {
		return destination;
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

	public String toString(){
		return "Flight: " + id.toString() + " Time: " + getDuration().getMinutes();
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
