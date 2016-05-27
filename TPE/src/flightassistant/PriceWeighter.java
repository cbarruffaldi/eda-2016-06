package flightassistant;

public class PriceWeighter implements Weighter<Flight, Airport>{

	@Override
	public GraphArc<Flight, Airport> minArc(GraphNode<Airport> from, GraphNode<Airport> to) {
		Airport origin = from.getValue();
		Airport destination = to.getValue();
		Flight cheapest = origin.getCheapestTo(destination);
		return new FlightArc(cheapest, cheapest.getPrice());
	}



}
