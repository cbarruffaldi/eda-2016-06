package flightassistant;

/**
 * Clase que evalúa dos aeropuertos dados y devuelve un {@link WeightedFlight} con el vuelo
 * mas barato entre ellos.
 * @see InfinityDijkstra
 * @see WeightedFlight
 */
public class PriceWeighter implements Weighter {
    public static final Weighter WEIGHTER = new PriceWeighter();

    private PriceWeighter () {
    }

    @Override public WeightedTicket minTicket (Airport from, Airport to) {
        Ticket cheapest = from.getCheapestTo(to);
        return new WeightedTicket(cheapest, cheapest.getPrice());
    }
}
