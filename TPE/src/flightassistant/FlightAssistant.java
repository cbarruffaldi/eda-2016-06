package flightassistant;

import structures.SimpleHashMap;
import utils.Moment;
import utils.Time;

import java.io.Serializable;
import java.util.Iterator;

public class FlightAssistant implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final int AIRPORTS_SIZE = 20;
	private static final int FLIGHTS_SIZE = 50; // TODO: pensar tamaños

	// Coleccion de Aeropuertos;
	public SimpleHashMap<String, Airport> airports;
	// Coleccion de vuelos
	public SimpleHashMap<FlightId, Flight> flights;

	public FlightAssistant() {
		airports = new SimpleHashMap<>(AIRPORTS_SIZE);
		flights = new SimpleHashMap<>(FLIGHTS_SIZE);
	}

	public void insertAirport(String id, double latitude, double longitude) {
		// Preguntar por inserción de dos aeropuertos con mismo ID.
		if (!airports.containsKey(id))
			airports.put(id, new Airport(id, latitude, longitude));
	}

	//Controla todo lo de las rutas aca de los dos aeropuerto. Queda un pcoo mas largo
	//pero para no delegar control de un aeropuerto en otro
	public void insertFlight(String airline, int number, double price, Moment[] departures, Time duration,
			 String origin, String destination) {

		Airport origAir = airports.get(origin);
		Airport destAir = airports.get(destination);
		if (origAir == null || destAir == null) {
			return; // podria devolver boolean para indicar si se pudo agregar el vuelo o no.
		}
		// Crea la ruta en caso de que todavía no exista
		if (!origAir.routeExistsTo(destAir)) {
			Route r = new Route(origAir, destAir);
			origAir.addRoute(destAir, r);
			destAir.addRoute(origAir, r);
		}
		Flight newFlight = new Flight(airline, number, price, departures[0], duration, origAir, destAir);
		// En este mapa se guardan sin tener en cuenta el día de partida.
		flights.put(newFlight.getId(), newFlight);

		for (Moment departMoment: departures) {
			Flight auxFl = new Flight(airline, number, price, departMoment, duration, origAir, destAir);
			origAir.addFlight(auxFl);
		}
	}

	public void removeFlight(String airline, int number) {
		FlightId flightId = new FlightId(airline, number);
		Flight flight = flights.get(flightId);
		if (flight != null) {
			Airport origAir = flight.getOrigin();
			origAir.removeFlight(flight);
			flights.remove(flightId);
		}
	}

	public void removeAirport(String id) {
		Airport airport = airports.get(id);
		if (airport != null) {
			removeRoutesTo(airport);
			airports.remove(id);
		}
	}

	private void removeRoutesTo(Airport airport) {
		Iterator<Airport> iter = airport.connectedAirportsIterator();
		while (iter.hasNext())
			iter.next().removeRouteTo(airport);
	}
	
	
	void refreshAirportsNodeProperties(){
			Iterator<Airport> airportsIter = airports.valueIterator();
			while(airportsIter.hasNext()){
				airportsIter.next().nodeRefresh();
			}
	}
}