package flightassistant;

public class WeightedFlight {
	private Flight flight;
	private double weight;
	
	public WeightedFlight(Flight flight, double weight){
		this.flight = flight;
		this.weight = weight;
	}

	public double weight() {
		return weight;
	}

	public Flight flight() {
		return flight;
	}
}
