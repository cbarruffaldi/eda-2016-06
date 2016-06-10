package flightassistant;

import utils.Moment;
import utils.Time;

import java.util.List;

/**
 * Un {@link Flight} representa a un vuelo. Cada vuelo es caracterizado por un numero y una aeroplinea.
 * Además cada vuelo tiene un precio y días en los que sale establecido.
 * Cada vuelo opera entre dos {@link Airport}s, uno desde donde sale (origen) y otro a donde llega
 * (destino)
 * @see Airport
 * @see FlightId
 * @see Moment
 * 
 */

public class Flight{

    private FlightId id;
    private double price;
    private List<Moment> departures;
    private Time duration;
    private Airport origin;
    private Airport destination;

    public Flight (String airline, int number, double price, List<Moment> departures, Time duration,
        Airport origin, Airport destination) {
        this.id = new FlightId(airline, number);
        this.price = price;
        this.duration = duration;
        this.departures = departures;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Retorna el aeropuerto desde donde sale el vuelo
     * @return <tt>Airport</tt> desde donde sale el vuelo
     */
    public Airport getOrigin () {
        return origin;
    }

    /**
     * Retorna el tiempo de salida de un vuelo
     * @return <tt>Time</tt> de salida del vuelo
     * @see Time
     */
    public Time getDepartureTime () {
        return departures.get(0).getTime();
    }

    /**
     * Retorna todos los momentos de salida del vuelo
     * @return List con todos los momentos de salida
     * @see Moment
     */
    public List<Moment> getDepartures () {
        return departures;
    }

    /**
     * Retorna el aeropuerto de destino
     * @return <tt>Airport</tt> destino
     */
    public Airport getDestination () {
        return destination;
    }

    /**
     * Comprueba si un vuelo sale en un {@link Moment} determinado
     * @param <tt>Moment<tt> para fijarse si un vuelo sale.
     * @return true si el vuelo sale en ese momento, false sino
     */
    public boolean departsAt (Moment moment) {
        for (Moment departure : departures)
            if (departure.equals(moment))
                return true;
        return false;
    }

    /**
     * Retorna la duracion del vuelo
     * @return duracion del vuelo
     */
    public Time getDuration () {
        return duration;
    }

    public FlightId getId () {
        return id;
    }

    /**
     * Retorna el precio del vuelo
     * @return
     */
    public double getPrice () {
        return price;
    }

    @Override public int hashCode () {
        return id.hashCode();
    }

    @Override public String toString () {
        return id.toString();
    }

    @Override public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        Flight other = (Flight) o;
        return id.equals(other.id);
    }
}
