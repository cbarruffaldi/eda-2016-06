package flightassistant;

import utils.Day;

import java.util.List;

/**
 * Idem {@link PriceWeighter} pero que solo considera vuelos en determinados días dados en
 * una {@link List<Day>}.
 * @see WeightedFlight
 * @see Day
 */
public class OriginPriceWeighter implements Weighter {
    private List<Day> days;

    public OriginPriceWeighter (List<Day> days) {
        this.days = days;
    }

    @Override public WeightedTicket minTicket (Airport from, Airport to) {
        Ticket cheapest = null;
        for (Day day : days) {
            Ticket aux = from.getCheapestTo(to, day);
            if (cheapest == null || (aux != null && aux.isCheaperThan(cheapest)))
                cheapest = aux;
        }
        return new WeightedTicket(cheapest, cheapest.getPrice());
    }
}
