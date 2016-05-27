package flightassistant;

import java.util.Iterator;

public interface GraphNode<T> {

	public T getValue();

	public boolean checked();
	public void check();
	public void uncheck();
	public Iterator<GraphNode<T>> getNeighbors();
	// Nos es más eficiente devolver un iterador que armar una Lista e iterar sobre la lista.
}
