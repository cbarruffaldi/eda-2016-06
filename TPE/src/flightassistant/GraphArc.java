package flightassistant;

public interface GraphArc<E,T> {

	public E getArc();

	public GraphNode<T> from();
	public GraphNode<T> to();
	public double getWeight();
}
