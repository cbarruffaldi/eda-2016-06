package flightassistant;

import utils.Moment;
import utils.Time;

import java.io.Serializable;
import java.util.List;

public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    private FlightId id;
    private double price;

    //private Schedule[] schedules;
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

    public Airport getOrigin () {
        return origin;
    }

    public Time getDepartureTime () {
        return departures.get(0).getTime();
    }

    public List<Moment> getDepartures () {
        return departures;
    }

    public Airport getDestination () {
        return destination;
    }

    public boolean departsAt (Moment moment) {
        for (Moment departure : departures)
            if (departure.equals(moment))
                return true;
        return false;
    }

    public Time getDuration () {
        return duration;
    }

    public FlightId getId () {
        return id;
    }

    public double getPrice () {
        return price;
    }

    @Override public int hashCode () {
        return id.hashCode();
    }

    @Override public String toString () {
        return "Flight: " + id.toString() + " Time: " + getDuration().getMinutes();
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
