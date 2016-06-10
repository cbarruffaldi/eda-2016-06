package flightassistant;

import utils.Moment;
import utils.Time;

import java.io.Serializable;

/**
 * Representa un pasaje con información acerca del vuelo y un {@link Moment} de partida específico
 *
 */
public class Ticket{

    private Flight flight;
    private Moment departure;

    public Ticket (Flight flight, Moment departure) {
        if (flight == null)
            throw new NullPointerException("null flight");
        if (departure == null)
            throw new NullPointerException("null departure");
        if (!flight.departsAt(departure))
            throw new IllegalArgumentException("Flight doesn't leave at given departure");

        this.flight = flight;
        this.departure = departure;
    }

    
    /**
     * Retorna el aeropuerto de origen del vuelo
     * @return </tt>Airport</tt> origen del vuelo
     */
    public Airport getOrigin () {
        return flight.getOrigin();
    }

    /**
     * Retorna el aeropuerto destino del vuelo
     * @return <tt>Airport</tt> destino del vuelo
     */
    public Airport getDestination () {
        return flight.getDestination();
    }

    /**
     * Retorna el id del vuelo
     * @return ID del vuelo
     */
    public FlightId getFlightId () {
        return flight.getId();
    }

    /**
     * Retorna el tiempo de partida del vuelo
     * @return tiempo de partida del vuelo
     */
    public Time getDepartureTime () {
        return flight.getDepartureTime();
    }

    /**
     * Compara dos Tickets respecto de su precio
     * @param <tt>Ticket</tt> con el cual se quiere comparar el precio
     * @return true si es mas barato que el ticket parámetro, false sino
     */
    public boolean isCheaperThan (Ticket other) {
        return Double.compare(getPrice(), other.getPrice()) < 0;
    }

    /**
     * Compara dos Tickets respecto de su tiempo de vuelo
     * @param <tt>Ticket</tt> con el cual se quiere comparar el tiempo de vuelo
     * @return true si tiene menor tiempo de vuelo que el ticket parámetro, false sino
     */
    public boolean isQuickerThan (Ticket other) {
        return getDuration().compareTo(other.getDuration()) < 0;
    }

    /**
     * Retorna el precio del vuelo
     * @return precio del vuelo
     */
    public double getPrice () {
        return flight.getPrice();
    }

    /**
     * Retorna el momento de partida del vuelo que corresponde al Ticket
     * @return <tt>Moment</tt> de partida del vuelo
     */
    public Moment getDeparture () {
        return departure;
    }

    public Time getDuration () {
        return flight.getDuration();
    }

    /**
     * Retorna el momento de llegada del vuelo a su destino
     * @return <tt>Moment</tt> de llegada al aeropuerto destino
     */
    public Moment getArrival () {
        return getDeparture().addTime(getDuration());
    }

    @Override public boolean equals (Object other) {
        if (other == this)
            return true;
        if (other == null || this.getClass() != other.getClass())
            return false;
        Ticket o = (Ticket) other;
        return departure.equals(o.departure) && flight.equals(o.flight);
    }

    @Override public int hashCode () {
        return flight.hashCode() ^ departure.hashCode();
    }

    @Override public String toString () {
        return flight.toString();
    }
}
