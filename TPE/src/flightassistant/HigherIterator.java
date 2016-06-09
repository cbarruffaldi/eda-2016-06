package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Moment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterador de tickets. Itera sobre un conjunto ordenado (por tiempo de salida)
 * de tickets, correspondientes a los vuelos entre dos determinados Airports,
 * a partir de un cierto Moment dado.
 * @see TotalTimeWeighter
 * @see Airport
 */
public class HigherIterator implements Iterator<Ticket> {
    private Iterator<AVLSet<Ticket>> dayIter;
    private Iterator<Ticket> ticketIter;

    public HigherIterator (Moment from, Day.WeekArray<AVLSet<Ticket>> weekArray) {

        List<Moment> dummyList = new ArrayList<>();
        // 	dummyList.add(new Moment(Day.LU, new Time(0,0)));
        dummyList.add(from);
        Flight dummyFlight = new Flight("A", 0, 0, dummyList, null, null, null);
        Ticket dummyTicket = new Ticket(dummyFlight, from);

        Day currentDay = from.getDay();
        Day nextDay = from.getDay().getNextDay();

        dayIter = weekArray.iteratorFrom(nextDay);
        ticketIter = weekArray.get(currentDay).higherIterator(dummyTicket);

        if (!ticketIter.hasNext())
            ticketIter = getNextIterator();
    }

    private Iterator<Ticket> getNextIterator () {
        while (dayIter.hasNext()) {
            Iterator<Ticket> iter = dayIter.next().iterator();
            if (iter.hasNext())
                return iter;
        }
        return null;
    }

    @Override public boolean hasNext () {
        return ticketIter != null;
    }

    @Override public Ticket next () {
        if (!hasNext())
            throw new NoSuchElementException();

        Ticket t = ticketIter.next();
        if (!ticketIter.hasNext())
            ticketIter = getNextIterator();
        return t;
    }
}
