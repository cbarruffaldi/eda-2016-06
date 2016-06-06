package flightassistant;

public class AirtimeWeighter implements Weighter {
	public static final Weighter WEIGHTER = new AirtimeWeighter();

	private AirtimeWeighter(){
	}

	@Override
	public WeightedTicket minTicket(Airport from, Airport to) {
		Ticket quickest = from.getQuickestTo(to);
		return new WeightedTicket(quickest, quickest.getDuration().getMinutes());
	}

}
