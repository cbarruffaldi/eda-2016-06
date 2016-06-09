package flightassistant;

/**
 * Clase que evalúa dos aeropuertos dados y devuelve un {@link WeightedFlight} con el vuelo
 * mas rápido (según tiempo de vuelo) entre ellos.
 * @see InfinityDijkstra
 * @see WeightedFlight
 */
public class AirtimeWeighter implements Weighter {
    public static final Weighter WEIGHTER = new AirtimeWeighter();

    private AirtimeWeighter () {
    }

    @Override public WeightedTicket minTicket (Airport from, Airport to) {
        Ticket quickest = from.getQuickestTo(to);
        return new WeightedTicket(quickest, quickest.getDuration().getMinutes());
    }

}
