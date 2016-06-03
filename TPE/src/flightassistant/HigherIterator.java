package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Moment;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Bianchi on 2/6/16.
 */
public class HigherIterator<T extends Flight> implements Iterator<T> {
    private Iterator<AVLSet<Flight>> dayIter;
	private Iterator<Flight> flIter;

    public HigherIterator(Moment from, Day.WeekArray<AVLSet<Flight>> weekArray) {
        Flight dummy = new Flight("DMY", 0, 0, from, null, null, null); // DUMMYYY preguntar
        dayIter = weekArray.iteratorFrom(from.getDay());
        if (dayIter.hasNext()) {
            flIter = dayIter.next().higherIterator(dummy);
        }
    }

    // Ver chequeos con el next.
    @Override
    public boolean hasNext() {
        return flIter.hasNext() || dayIter.hasNext();
    }

    @Override
    public T next() {
        if (flIter.hasNext()) {
            return (T) flIter.next();
        } else if (dayIter.hasNext()) {
            flIter = dayIter.next().iterator();
            return (T) flIter.next(); // ?? Chequear next de nuevo
        } else {
            throw new NoSuchElementException();
        }
    }
}
