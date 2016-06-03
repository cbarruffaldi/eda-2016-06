package flightassistant;

import utils.Moment;
import utils.Time;

/**
 * Created by Bianchi on 2/6/16.
 */
public class TotalTimeWeighter implements Weighter {
    public static final Weighter WEIGHTER = new TotalTimeWeighter();

    @Override
    public WeightedFlight minFlight(Airport from, Airport to) {
        Flight previous = from.getIncident();
//        if (previous == null) {
//            // El tema del primer aeropuerto que no anda con djiasakjst normal
//        }
        Moment startMoment = previous.getDeparture().addTime(previous.getDuration());

        HigherIterator<Flight> flightsIter = from.iteratorOfHigherFlightsTo(to, startMoment);

        if (!flightsIter.hasNext())
            return null;

        Flight min = flightsIter.next();
        Time shortestTime = totalTime(startMoment, min);
        while (flightsIter.hasNext()) {
            Flight flight = flightsIter.next();
            Time aux = totalTime(startMoment, flight);
            if (aux.compareTo(shortestTime) < 0) {
                min = flight;
                shortestTime = aux;
            }
        }

        return new WeightedFlight(min, min.getDuration().getMinutes());
    }

    private Time totalTime(Moment start, Flight flight) {
        Time wait = start.howMuchUntil(flight.getDeparture());
        return wait.addTime(flight.getDuration());
    }
}
