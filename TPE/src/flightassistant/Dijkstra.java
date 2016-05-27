package flightassistant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

@Deprecated
public class Dijkstra {

	private static class LinkedArc<E,T> implements Comparable<LinkedArc<E,T>>{
		private GraphArc<E,T> arc;
		private LinkedArc<E,T> prev;
		private double weight;

		public LinkedArc(GraphArc<E,T> arc, LinkedArc<E,T> prev, double weight) {
			this.arc = arc;
			this.prev = prev;
			this.weight = weight;
		}

		@Override
		public int compareTo(LinkedArc<E,T> o) {
			return Double.compare(weight, o.weight);
		}
	}

	// TODO: Precondición: Nodos desmarcados!
	public static <E,T> List<E> minPath (GraphNode<T> from, GraphNode<T> to,
			Weighter<E,T> weighter) {

		if (from == null || to == null)
			throw new IllegalArgumentException("null arguments");

		PriorityQueue<LinkedArc<E,T>> pq = new PriorityQueue<>(); //TODO: pasar tamaño como parámetro

		from.check();
		enqueueArcs(pq, from, null, weighter, 0);

		while (!pq.isEmpty()) {
			LinkedArc<E,T> linkedArc = pq.remove();  // arista de menor peso acumulado
			double weight = linkedArc.weight;
			GraphNode<T> node = linkedArc.arc.from();

			if (!node.checked()) {
				node.check();
				if (node.equals(to))
					return buildList(linkedArc);
				enqueueArcs(pq, node, linkedArc, weighter, weight);
			}
		}

		return null; // camino no encontrado
	}
	// TODO: desmarcar nodos!

	private static <E,T> void enqueueArcs(PriorityQueue<LinkedArc<E,T>> pq, GraphNode<T> from,
			LinkedArc<E,T> prev, Weighter<E,T> weighter, double acumWeight) {

		Iterator<GraphNode<T>> iter = from.getNeighbors();

		while (iter.hasNext()) {
			GraphNode<T> node = iter.next();
			if (!node.checked()) {
				GraphArc<E,T> minArc = weighter.minArc(from, node);
				pq.add(new LinkedArc<E,T>(minArc, prev, minArc.getWeight() + acumWeight));
			}
		}
	}

	private static <E,T> List<E> buildList(LinkedArc<E,T> arc) {
		LinkedList<E> list = new LinkedList<E>();

		LinkedArc<E,T> curr = arc;
		while(curr.prev != null){
			list.addFirst(curr.arc.getArc());
			curr = curr.prev;
		}
		return list;
	}
}
