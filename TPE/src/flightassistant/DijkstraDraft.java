package flightassistant;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import structures.AVLMap;
import structures.AVLSet;
import structures.SimpleHashMap;
import structures.SimpleMap;

//Borrador/idea a partir de lo qeu vimos en clase
public class DijkstraDraft {
	FlightAssistant FlightAssistant;

	private static class Node{
		Airport airport;
		Node predecessor;

		public Node(Airport airport, Airport predecessor){
			this.airport = airport;
			visited = false;
			this.predecessor = predecessor;
		}

		public boolean equals(Object o){
			if(o == null || !(o instanceof Node)) return false;

			return airport.equals(((Node)o).airport);

		}
	}

	private static class PQNode implements Comparable<PQNode>{

		private Node node;

		public PQNode(Node n){
			node = n;
		}

		@Override
		public int compareTo(PQNode o) {
			//aca viene la magia
			return 0;
		}

	}

	Set<Airport> visited = new AVLSet<Airport>();

	public List<Airport> minDistance(String from, String to){
		Airport originAirport = FlightAssistant.airports.get(from),
				targetAirport = FlightAssistant.airports.get(to);

		if(originAirport == null || targetAirport == null) return null;
		PriorityQueue<PQNode> pq = new PriorityQueue<PQNode>(); //TODO: pasar tamaño como parámetro

		Node origin = new Node(originAirport,null);
		pq.offer(new PQNode(origin));


		while(!pq.isEmpty()){
			PQNode n = pq.poll();
			if(n.node.airport.equals(targetAirport)){
				return buildList(n.node);
			}

			Airport airport = n.node.airport;
			if(!visited.contains(airport)){
				for(int i = 0 ; i < 1 ; i++ ){//TODO: Iterar sobre los vuelos correspodientes y:
					Airport newAir = null; //nuevo aeropuerto para la queue
					pq.offer(new PQNode(new Node(newAir, airport)));
				}
			visited.add(airport);
			}
		}

		return null;

}



	private List<Airport> buildList(Node node) {
		LinkedList<Airport> list = new LinkedList<Airport>();

		Node curr = node;
		while(curr.predecessor != null){
			list.offerFirst(node.airport);
			curr = node.predecessor;
		}
		return list;
	}

}
