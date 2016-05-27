package flightassistant;

import java.io.Serializable;

public class FlightId implements Comparable<FlightId>, Serializable{

	private static final long serialVersionUID = 1L;
	
	private String airline;
	private int number;
	
	public FlightId(String airline, int number){
		if (airline == null)
			throw new IllegalArgumentException("null airline");
		if (number < 0)
			throw new IllegalArgumentException("Invalid flight number");
		this.airline = airline;
		this.number = number;
	}
	
	public String getAirline() {
		return airline;
	}
	
	public int getNumber() {
		return number;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (!other.getClass().equals(getClass()))
			return false;
		FlightId o = (FlightId) other;
		return airline.equals(o.airline) && number == o.number;
	}
	
	@Override
	public int hashCode() {
		return airline.hashCode() + number; // TODO: hacerlo bien
	}

	@Override
	public int compareTo(FlightId o) {
		int strComp = getAirline().compareTo(o.getAirline());
		return strComp == 0 ? Integer.compare(getNumber(), o.getNumber()) : strComp;
	}
}
