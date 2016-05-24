package flightassistant;

public class FlightId {
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
}
