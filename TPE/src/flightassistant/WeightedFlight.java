package flightassistant;

/**
 * Representa un {@link Flight} con un peso asociado para poder ser comparado
 * @see InfinityDijkstra
 * @see Flight
 *
 */
public class WeightedFlight {
    private Flight flight;
    private double weight;

    public WeightedFlight (Flight flight, double weight) {
        this.flight = flight;
        this.weight = weight;
    }

    public double weight () {
        return weight;
    }

    public Flight flight () {
        return flight;
    }
}
