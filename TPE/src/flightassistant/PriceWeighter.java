package flightassistant;

public class PriceWeighter implements Weighter{

	@Override
	public double minWeight(Route r, Airport origin) {
		return r.getCheapest(origin).getPrice();
	}

	@Override
	public Flight getMinumum(Route r, Airport origin) {
		return r.getCheapest(origin);
	}
	
}
