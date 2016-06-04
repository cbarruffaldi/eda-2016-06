package flightassistant;

import structures.BinaryMinHeap;
import utils.Day;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InfinityDijkstra {

	public static List<Ticket> minPath(FlightAssistant fa, Airport origin, Airport dest, Weighter weighter) {
		return minPath2(fa, origin, dest, weighter, null, null);
	}

	public static List<Ticket> minPath(FlightAssistant fa, Airport origin, Airport dest, Weighter weighter,
			Weighter originWeighter, List<Day> departureDays) {

		BinaryMinHeap<Airport> pq = queueAirports(fa);

		pq.decreasePriority(origin, 0);

		// TODO: se podría hacer una función para no repetir código con el de abajo

		if (departureDays != null) {
			pq.dequeue(); // dequeue origin
			origin.visit();
			Iterator<Airport> iter = origin.connectedAirportsIterator();
			while (iter.hasNext()) {
				Airport next = iter.next();
				if (origin.flightExistsTo(next)) {
					WeightedTicket wTicket = originWeighter.minTicket(origin, next);
					next.setIncident(wTicket.ticket());
					pq.decreasePriority(next, wTicket.weight());
				}
			}
		}

		while(!pq.isEmpty()){
			Double minWeight = pq.minWeight();

			if(Double.compare(minWeight, Double.POSITIVE_INFINITY) == 0)
				return null; //No existe el camino, no tiene sentido seguir

			Airport airport = pq.dequeue();
			airport.visit();

			if(airport.equals(dest)){
				return buildList(dest);
			}

			Iterator<Airport> iter = airport.connectedAirportsIterator();
				while(iter.hasNext()){
					Airport next = iter.next();
					if (!next.visited() && airport.flightExistsTo(next)) {
						WeightedTicket wTicket = weighter.minTicket(airport, next);

						double nextCurrWeight = pq.getPriority(next);
						double acumWeight = minWeight + wTicket.weight();
						if(acumWeight < nextCurrWeight){
							next.setIncident(wTicket.ticket());
							pq.decreasePriority(next, acumWeight);
						} //end if
					} // end if
				} // end while
			} //end if

		return null;
	}

	// Sin repetir codigo, testear
	public static List<Ticket> minPath2(FlightAssistant fa, Airport origin, Airport dest, Weighter weighter,
									   Weighter originWeighter, List<Day> departureDays) {

		BinaryMinHeap<Airport> pq = queueAirports(fa);

		pq.decreasePriority(origin, 0);

		List<Ticket> ans = null;
		if (departureDays != null) {
			djistra(pq, dest, originWeighter, true);
		}
		ans = djistra(pq, dest, weighter, false);

		return ans;
	}

	// Cambiar nombre
	private static List<Ticket> djistra(BinaryMinHeap<Airport> pq, Airport dest, Weighter weighter, boolean isOrigin) {

		while (!pq.isEmpty()) {
			Double minWeight = pq.minWeight();

			if (Double.compare(minWeight, Double.POSITIVE_INFINITY) == 0)
				return null; //No existe el camino, no tiene sentido seguir

			Airport airport = pq.dequeue();
			airport.visit();

			if (airport.equals(dest)) {
				return buildList(dest);
			}

			Iterator<Airport> iter = airport.connectedAirportsIterator();
			while (iter.hasNext()) {
				Airport next = iter.next();
				if (!next.visited() && airport.flightExistsTo(next)) {
					WeightedTicket wTicket = weighter.minTicket(airport, next);

					double nextCurrWeight = pq.getPriority(next);
					double acumWeight = minWeight + wTicket.weight();

					if (acumWeight < nextCurrWeight) {
						next.setIncident(wTicket.ticket());
						pq.decreasePriority(next, acumWeight);
					}
				}
			}
			// Si está en el aeropuerto del origen tiene que ciclar una sola vez, y devuelve null.
			if (isOrigin) return null;
		}
		return null;
	}

	private static BinaryMinHeap<Airport> queueAirports(FlightAssistant fa) {
		int size = fa.airports.size();

		BinaryMinHeap<Airport> heap = new BinaryMinHeap<>(size);

		Iterator<Airport> iter = fa.airports.valueIterator();

		while(iter.hasNext()){
			heap.enqueue(iter.next(), Double.POSITIVE_INFINITY);
		}

		return heap;
	}




	private static List<Ticket> buildList(Airport last) {
		LinkedList<Ticket> list = new LinkedList<>();

		Airport curr = last;
		Ticket t;
		while((t = curr.getIncident()) != null) {
			list.addFirst(t);
			curr = t.getOrigin();
		}
		return list;
	}

}
