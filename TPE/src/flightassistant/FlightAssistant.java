package flightassistant;

import structures.SimpleHashMap;
import utils.Moment;
import utils.Time;

public class FlightAssistant {
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
		Flight newFlight = new Flight(airline, number, price, departures, duration, origAir, destAir);
		flights.put(newFlight.getId(), newFlight); // Preguntar por inserción de vuelo existente

		if (! origAir.routeExistsTo(destAir)) {
			Route r = new Route(origAir, destAir);
			origAir.addRoute(destAir, r);
			destAir.addRoute(origAir, r);
		}

		origAir.addFlight(newFlight);
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
		for (Airport each : airport.getDestinations())
			each.removeRouteTo(airport);
	}
}