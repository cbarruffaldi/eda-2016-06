package flightassistant;

import java.util.List;

import utils.Day;

public class OriginPriceWeighter implements Weighter {
	private List<Day> days;
	
	public OriginPriceWeighter(List<Day> days) {
		this.days = days;
	}


	@Override
	public WeightedFlight minFlight(Airport from, Airport to) {
		Flight cheapest = from.getCheapestTo(to, days.get(0));
		for (Day day : days) {
			Flight aux = from.getCheapestTo(to, day);
			if (aux.getPrice() < cheapest.getPrice())
				cheapest = aux;
		}
		return new WeightedFlight(cheapest, cheapest.getPrice());
	}

}
