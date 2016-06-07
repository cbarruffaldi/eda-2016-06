package flightassistant;

import utils.Day;
import utils.Moment;
import utils.Time;

//Tiene que salir si o si ese dia
//Repetimos codigo. Mala suerte :(
public class OriginDynamicWeighter {

    private static final int MINUTES_IN_A_DAY = 24 * 60;
    Day departureDay;

    public OriginDynamicWeighter (Day departure) {
        this.departureDay = departure;
    }

    public Double weight (Airport from, Airport to, Moment startMoment) {

        if (!from.flightExistsTo(to))
            return Double.POSITIVE_INFINITY;

        HigherIterator ticketIter = from.iteratorOfHigherFlightsTo(to, startMoment);

        if (!ticketIter.hasNext())
            return Double.POSITIVE_INFINITY;

        Ticket min = ticketIter.next();
        Time shortestTime = totalTime(startMoment, min);

        while (ticketIter.hasNext()
            && startMoment.getTime().getMinutes() + shortestTime.getMinutes() <= MINUTES_IN_A_DAY) {
            Ticket ticket = ticketIter.next();
            Time waitTime = startMoment.howMuchUntil(ticket.getDeparture());

            Time aux = totalTime(startMoment, ticket);
            if (aux.compareTo(shortestTime) < 0) {
                min = ticket;
                shortestTime = aux;
            }
        }

        if (startMoment.getTime().getMinutes() + shortestTime.getMinutes()
            > MINUTES_IN_A_DAY) { //Me pase del d�a
            return Double.POSITIVE_INFINITY;
        }

        return (double) shortestTime.getMinutes();

    }

    private Time totalTime (Moment start, Ticket ticket) {
        Time wait = start.howMuchUntil(ticket.getDeparture());
        return wait.addTime(ticket
            .getDuration()); //Se hace esto en lugar de ticket.getArrival por si dura mas de una semana?
    }
}
