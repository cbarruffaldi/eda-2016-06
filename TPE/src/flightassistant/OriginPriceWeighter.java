package flightassistant;

import java.util.List;

import utils.Day;

public class OriginPriceWeighter implements Weighter {
	private List<Day> days;

	public OriginPriceWeighter(List<Day> days) {
		this.days = days;
	}

	@Override
	public WeightedTicket minTicket(Airport from, Airport to) {
		Ticket cheapest = null;
		for (Day day : days) {
			Ticket aux = from.getCheapestTo(to, day);
			if (cheapest == null || (aux != null && aux.isCheaperThan(cheapest)))
				cheapest = aux;
		}
		return new WeightedTicket(cheapest, cheapest.getPrice());
	}
}
