package flightassistant;

import java.util.List;

public interface GraphNode<T,E> {
	public boolean checked();
	public void check();
	public void uncheck();
	public List<GraphArc<T,E>> getNeighbors();
}
