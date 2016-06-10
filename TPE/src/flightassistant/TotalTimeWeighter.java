package flightassistant;

import utils.Moment;
import utils.Time;

/**
 * Se encarga de elegir el mejor camino respecto del tiempo total, es decir
 * respecto de su momento de salida, tiempo de vuelo y tiempo de espera
 * @see InfinityDijkstra
 *
 */
public class TotalTimeWeighter implements Weighter {
    public static final Weighter WEIGHTER = new TotalTimeWeighter();

    @Override public WeightedTicket minTicket (Airport from, Airport to) {
        Ticket previous = from.getIncident();

        Moment startMoment = previous.getArrival();

        HigherIterator ticketIter = from.iteratorOfHigherFlightsTo(to, startMoment);

        if (!ticketIter.hasNext())
            return null;

        Ticket min = ticketIter.next();
        Time shortestTime = totalTime(startMoment, min);
        while (ticketIter.hasNext()) {
            Ticket ticket = ticketIter.next();
            Time waitTime = startMoment.howMuchUntil(ticket.getDeparture());

            // Dado que los vuelos se iteran se forma ordenada por momento de salida,
            // los waitTime son crecientes. Si un waitTime es mayor a el
            // menor total time ya puedo cortar y retornar; todos los siguientes
            // vuelos tendrán mayor waitTime y por ende mayor total time.
            if (waitTime.compareTo(shortestTime) >= 0)
                return new WeightedTicket(min, shortestTime.getMinutes());

            Time aux = totalTime(startMoment, ticket);
            if (aux.compareTo(shortestTime) < 0) {
                min = ticket;
                shortestTime = aux;
            }
        }

        return new WeightedTicket(min, shortestTime.getMinutes());
    }

    private Time totalTime (Moment start, Ticket ticket) {
        Time wait = start.howMuchUntil(ticket.getDeparture());
        return wait.addTime(ticket.getDuration());
    }
}
