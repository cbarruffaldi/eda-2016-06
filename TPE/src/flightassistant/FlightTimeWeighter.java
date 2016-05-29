package flightassistant;

public class FlightTimeWeighter implements Weighter {
	public static final Weighter WEIGHTER = new FlightTimeWeighter();

	@Override
	public WeightedFlight minFlight(Airport from, Airport to) {
		Flight quickest = from.getQuickestTo(to);
		return new WeightedFlight(quickest, quickest.getDuration().getMinutes());
	}
	
}
