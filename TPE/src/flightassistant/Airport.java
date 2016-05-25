package flightassistant;

import structures.SimpleHashMap;

import java.util.Comparator;

public class Airport {

	private String id;
	private double latitude;
	private double longitude;
	private SimpleHashMap<String, Route> routes;

	public Airport(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;

		// Tambien se podría hacer que Airport sea comparable y usar eso en vez de String crudo. (de todas maneras compararía el mismo string)
		routes = new SimpleHashMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public String getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void addFlight(Flight flight) {
		Airport destination = flight.getDestination();
		Route r = routes.get(destination.id);
		if (r == null) {
			r = new Route(this, destination);
			// Agrega la ruta a los dos aeropuertos que conecta.
			this.addRoute(destination.id, r);
			destination.addRoute(this.id, r);
		}
		r.addFlight(flight);
	}
	
	
	
	public void addFlight2(Flight flight) {
		Airport destination = flight.getDestination();
		if (! routeExistsTo(destination.id)) { //Un acceso mas, pero queda mas claro
			throw new IllegalStateException();
		}

		Route r = routes.get(destination.id);
		r.addFlight(flight);
	}
	
	
	public boolean routeExistsTo(String id) {
		return routes.get(id) != null;
	}

	public void addRoute(String airportId, Route r) {
		routes.put(airportId, r);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*
	 * Determina la igualdad de dos Aeropuertos según su nombre (id)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Airport other = (Airport) o;
		return id == null ? other.id == null : id.equals(other.id);
	}


}
