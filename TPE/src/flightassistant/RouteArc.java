package flightassistant;

//Peso por precio
public class RouteArc implements GraphArc<Flight, Airport>{

	Route route;
	Airport origin;
	Airport dest;
	Weighter weighter;
	
	@Override
	public Airport from() {
		return origin;
	}

	@Override
	public Airport to() {
		return dest;
	}
	
	@Override
	public double getWeight() {
		return weighter.minWeight(route, origin);
	}
	
	public Flight getArc(){
		return weighter.getMinumum(route, origin);
	}
	
	public RouteArc(Airport origin, Airport dest){
		this.route = route;
		this.origin = origin;
	}
	
}
