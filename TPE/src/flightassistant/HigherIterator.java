package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Moment;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Bianchi on 2/6/16.
 */
public class HigherIterator implements Iterator<Flight> {
    private Iterator<AVLSet<Flight>> dayIter;
	private Iterator<Flight> flIter;

    public HigherIterator(Moment from, Day.WeekArray<AVLSet<Flight>> weekArray) {
        Flight dummy = new Flight("A", 0, 0, from, null, null, null); // DUMMYYY preguntar
        Day currentDay = from.getDay();
        Day nextDay = from.getDay().getNextDay();

        dayIter = weekArray.iteratorFrom(nextDay);
        flIter = weekArray.get(currentDay).higherIterator(dummy);

        if (!flIter.hasNext())
        	flIter = getNextIterator();
    }

    private Iterator<Flight> getNextIterator() {
    	while (dayIter.hasNext()) {
    		Iterator<Flight> iter = dayIter.next().iterator();
    		if (iter.hasNext())
    			return iter;
    	}
    	return null;
	}

	// Ver chequeos con el next.
    @Override
    public boolean hasNext() {
        return flIter != null;
    }

    @Override
    public Flight next() {
    	if (!hasNext())
    		throw new NoSuchElementException();

    	Flight f = flIter.next();
    	if (!flIter.hasNext())
    		flIter = getNextIterator();
    	return f;
    }
}
