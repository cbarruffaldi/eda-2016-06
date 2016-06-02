package flightassistant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import structures.BinaryMinHeap;

public class InfinityDijkstra {
	
	
	
	public static List<Flight> minPath(FlightAssistant fa, Airport origin, Airport dest, Weighter weighter){
		BinaryMinHeap<Airport> pq = queueAirports(fa);
		
		
		pq.decreasePriority(origin, 0);
		
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
					if(!next.visited() && airport.flightExistsTo(next)){
						WeightedFlight wflight = weighter.minFlight(airport, next);
						
						double nextCurrWeight = pq.getPriority(next);
						double acumWeight = minWeight + wflight.weight();
						if(acumWeight < nextCurrWeight){
							next.setIncident(wflight.flight());
							pq.decreasePriority(next, acumWeight);
						} //end if 									
					} // end if
				} // end while
			} //end if			

		return null;
	}
	
	

	
	private static BinaryMinHeap<Airport> queueAirports(FlightAssistant fa) {
		int size = fa.airports.size();
	
		BinaryMinHeap<Airport> heap = new BinaryMinHeap<Airport>(size);
		
		Iterator<Airport> iter = fa.airports.valueIterator();
	
		while(iter.hasNext()){
			heap.enqueue(iter.next(), Double.POSITIVE_INFINITY);		
		}
		
		return heap;
	}




	private static List<Flight> buildList(Airport last) {
		LinkedList<Flight> list = new LinkedList<>();

		Airport curr = last;
		Flight f;
		while((f = curr.getIncident()) != null){
			list.addFirst(f);
			curr = f.getOrigin();
		}
		return list;
	}

}
