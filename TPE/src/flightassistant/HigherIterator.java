package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Moment;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HigherIterator implements Iterator<Ticket> {
    private Iterator<AVLSet<Ticket>> dayIter;
	private Iterator<Ticket> ticketIter;

    public HigherIterator(Moment from, Day.WeekArray<AVLSet<Ticket>> weekArray) {
        Flight dummyFlight = new Flight("A", 0, 0, null, null, null, null);
        Ticket dummyTicket = new Ticket(dummyFlight, from);
        Day currentDay = from.getDay();
        Day nextDay = from.getDay().getNextDay();

        dayIter = weekArray.iteratorFrom(nextDay);
        ticketIter = weekArray.get(currentDay).higherIterator(dummyTicket);

        if (!ticketIter.hasNext())
        	ticketIter = getNextIterator();
    }

    private Iterator<Ticket> getNextIterator() {
    	while (dayIter.hasNext()) {
    		Iterator<Ticket> iter = dayIter.next().iterator();
    		if (iter.hasNext())
    			return iter;
    	}
    	return null;
	}

    @Override
    public boolean hasNext() {
        return ticketIter != null;
    }

    @Override
    public Ticket next() {
    	if (!hasNext())
    		throw new NoSuchElementException();

    	Ticket t = ticketIter.next();
    	if (!ticketIter.hasNext())
    		ticketIter = getNextIterator();
    	return t;
    }
}
