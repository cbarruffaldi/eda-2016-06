package flightassistant;

public interface Weighter<E,T> {
	public GraphArc<E,T> minArc(GraphNode<T> from, GraphNode<T> to);
}
