package flightassistant;

public interface GraphArc<T> {

	public T from();	
	public T to();
	public int getWeight();
	
}
