package flightassistant;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


public class DijsktraForReal {
	
//	private static class Node implements Comparable<Node>{
//		double weight;
//		Airport airport;
//		
//		public boolean visited(){
//			return airport.checked();
//		}
//		
//		public void visit(){
//			airport.check();
//		}
//	
//		public Iterator<Airport> getNeighbors(){
//			return airport.getDestinationsIterator();
//			
//		}
//		
//		public Node(Airport airport, double weight){
//			this.airport = airport;
//			this.weight = weight;
//		}
//
//		@Override
//		public int compareTo(Node o) {
//			return Double.compare(this.weight, o.weight);
//		}
//
//		public void updateIncident(Flight flight, double acumWeight) {
//			if(airport.getIncident() == null)
//				airport.setIncident(flight);
//			
//			
//		}
//	}
	
	
	//Precondicion: aeropuertos desmarcados
	public static List<Flight> minPath(Airport origin, Airport dest, Weighter weighter){
		PriorityQueue<Airport> pq = new PriorityQueue<Airport>(new Comparator<Airport>() {

			@Override
			public int compare(Airport o1, Airport o2) {
				return Double.compare(o1.weight(), o2.weight());
			}
			
		});
		
		pq.add(origin);
		origin.setWeight(0);
		
		while(!pq.isEmpty()){
			Airport airport = pq.remove();
			if(airport.equals(dest)){
				return buildList(dest);
			}
			
			if(!airport.visited()){
				airport.visit();
				Iterator<Airport> iter = airport.getDestinationsIterator();
				while(iter.hasNext()){
					Airport next = iter.next();
					if(!next.visited() && airport.flightExistsTo(next)){
						WeightedFlight wflight = weighter.minFlight(airport, next);
						double acumWeight = airport.weight() + wflight.weight();
						if(acumWeight < next.weight()){
							next.setWeight(acumWeight);
							next.setIncident(wflight.flight());
							pq.add(next);
						} //end if 									
					} // end if
				} // end while
			} //end if			
		} //end while queue

		return null;
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
