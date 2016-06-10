package flightassistant;

/**
 * Permite elegir el la mejor opción de {@link Ticket} desde un {@link Airport} a otro
 * @see InfinityDijkstra
 * @see Airport
 *
 */
public interface Weighter {
    WeightedTicket minTicket (Airport from, Airport to);
}
