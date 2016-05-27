package flightassistant;

public class PriceWeighter implements Weighter{

	@Override
	public WeightedFlight minFlight(Airport from, Airport to) {
		Flight cheapest = from.getCheapestTo(to);
		return new WeightedFlight(cheapest, cheapest.getPrice());
	}

}
