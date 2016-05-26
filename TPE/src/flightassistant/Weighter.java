package flightassistant;

public interface Weighter {
	public double minWeight(Route route, Airport origin);
	public Flight getMinumum(Route route, Airport origin);
}
