package flightassistant;

import utils.Moment;
import utils.Time;

public class Ticket {
	private Flight flight;
	private Moment departure;

	public Ticket(Flight flight, Moment departure) {
		if (flight == null)
			throw new NullPointerException("null flight");
		if (departure == null)
			throw new NullPointerException("null departure");
		if (!flight.departsAt(departure))
			throw new IllegalArgumentException("Flight doesn't leave at given departure");

		this.flight = flight;
		this.departure = departure;
	}

	public Airport getOrigin() {
		return flight.getOrigin();
	}

	public Airport getDestination() {
		return flight.getDestination();
	}

	public FlightId getFlightId() {
		return flight.getId();
	}

	public Time getDepartureTime() {
		return flight.getDepartureTime();
	}

	public boolean isCheaperThan(Ticket other) {
		return Double.compare(getPrice(), other.getPrice()) < 0;
	}

	public boolean isQuickerThan(Ticket other) {
		return getDuration().compareTo(other.getDuration()) < 0;
	}

	public double getPrice() {
		return flight.getPrice();
	}

	public Moment getDeparture() {
		return departure;
	}

	public Time getDuration() {
		return flight.getDuration();
	}

	public Moment getArrival() {
		return getDeparture().addTime(getDuration());
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null || this.getClass() != other.getClass())
			return false;
		Ticket o = (Ticket) other;
		return departure.equals(o.departure) && flight.equals(o.flight);
	}

	@Override
	public int hashCode() {
		return flight.hashCode() ^ departure.hashCode();
	}
}
