package flightassistant;

public class FlightArc implements GraphArc<Flight, Airport>{

	private Flight flight;
	private double weight;

	public FlightArc(Flight f, double w) {
		flight = f;
		weight = w;
	}

	@Override
	public Flight getArc() {
		return flight;
	}

	@Override
	public GraphNode<Airport> from() {
		return flight.getOrigin();
	}

	@Override
	public GraphNode<Airport> to() {
		return flight.getDestination();
	}

	@Override
	public double getWeight() {
		return weight;
	}
}
