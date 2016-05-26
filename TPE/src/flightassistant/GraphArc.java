package flightassistant;

public interface GraphArc<E,T> {
	
	public E getArc();
	
	public T from();	
	public T to();
	public double getWeight();
	
}
