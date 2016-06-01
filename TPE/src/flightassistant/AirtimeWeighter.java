package flightassistant;

public class AirtimeWeighter implements Weighter {
	public static final Weighter WEIGHTER = new AirtimeWeighter();

	@Override
	public WeightedFlight minFlight(Airport from, Airport to) {
		Flight quickest = from.getQuickestTo(to);
		return new WeightedFlight(quickest, quickest.getDuration().getMinutes());
	}

}
