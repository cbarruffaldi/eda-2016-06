package flightassistant;

import utils.Day;

import java.util.List;

/**
 * Idem {@link AirtimeWeighter} pero que solo considera vuelos en determinados días dados en
 * una {@link List<Day>}.
 * @see WeightedFlight
 * @see Day
 */
public class OriginAirtimeWeighter implements Weighter {
    private List<Day> days;

    public OriginAirtimeWeighter (List<Day> days) {
        this.days = days;
    }

    @Override public WeightedTicket minTicket (Airport from, Airport to) {
        Ticket quickest = null;
        for (Day day : days) {
            Ticket aux = from.getQuickestTo(to, day); // devuelve null si no hay vuelos ese día
            if (quickest == null || (aux != null && aux.isQuickerThan(quickest)))
                quickest = aux;
        }
        return new WeightedTicket(quickest, quickest.getDuration().getMinutes());
    }

}
