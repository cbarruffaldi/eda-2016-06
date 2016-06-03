package flightassistant;

import java.util.List;

import utils.Day;

public class OriginQuickestWeighter implements Weighter {
	private List<Day> days;
	
	public OriginQuickestWeighter(List<Day> days) {
		this.days = days;
	}

	@Override
	public WeightedFlight minFlight(Airport from, Airport to) {
		Flight quickest = from.getQuickestTo(to, days.get(0));
		for (Day day : days) {
			Flight aux = from.getQuickestTo(to, day);
			if (aux.getPrice() < quickest.getPrice())
				quickest = aux;
		}
		return new WeightedFlight(quickest, quickest.getPrice());
	}

}
