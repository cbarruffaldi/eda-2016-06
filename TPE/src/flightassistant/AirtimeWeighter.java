package flightassistant;

public class AirtimeWeighter implements Weighter<Flight, Airport> {

	@Override
	public GraphArc<Flight, Airport> minArc(GraphNode<Airport> from, GraphNode<Airport> to) {
		Airport origin = from.getValue();
		Airport destination = to.getValue();
		Flight quickest = origin.getQuickestTo(destination);
		
		//TODO: Devolver double no se si esta tan bueno. Impelemtar Weigher<Arc,Node,WeightType>?
		return new FlightArc(quickest, quickest.getDuration().getMinutes());
	}

}
