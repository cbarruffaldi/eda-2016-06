package flightassistant;

import java.util.List;

public interface GraphNode<T> {
	public boolean visited();
	public List<GraphArc<T>> getNeighbors();
}
