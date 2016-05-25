package flightassistant;

import structures.SimpleHashMap;
import utils.Moment;
import utils.Time;

import java.util.Comparator;

public class FlightAssistant {
	private static final int AIRPORTS_SIZE = 20;
	private static final int FLIGHTS_SIZE = 50; // TODO: pensar tamaños

	// Coleccion de Aeropuertos;
	public SimpleHashMap<String, Airport> airports;
	// Coleccion de vuelos
	public SimpleHashMap<FlightId, Flight> flights;

	public FlightAssistant() {
		airports = new SimpleHashMap<>(AIRPORTS_SIZE, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		
		flights = new SimpleHashMap<>(FLIGHTS_SIZE, new Comparator<FlightId>() {
			@Override
			public int compare(FlightId o1, FlightId o2) {
				int strComp = o1.getAirline().compareTo(o2.getAirline());
				return strComp == 0 ? Integer.compare(o1.getNumber(), o2.getNumber()) : strComp;
			}
		});
	}

	public void insertAirport(String id, double latitude, double longitude) {
		// Preguntar si puede suceder que los gauchos agreguen dos aeropuertos con mismo ID.
		// Preguntar qué hacer en ese caso también
		if (!airports.containsKey(id))
			airports.put(id, new Airport(id, latitude, longitude));
	}
	
	public void insertFlight(String airline, int number, double price, Moment[] departures, Time duration,
							 String origin, String destination) {
		Airport origAir = airports.get(origin);
		Airport destAir = airports.get(destination);
		if (origAir == null || destAir == null) {
			return; // podría devolver boolean para indicar si se pudo agregar el vuelo o no.
		}
		Flight newFlight = new Flight(airline, number, price, departures, duration, origAir, destAir);
		flights.put(newFlight.getId(), newFlight);
		
		origAir.addFlight(newFlight);
	}
	
	
	//Controla todo lo de las rutas aca de los dos aeropuerto. Queda un pcoo mas largo
	//pero para no delegar control de un aeropuerto en otro
	public void insertFlight2(String airline, int number, double price, Moment[] departures, Time duration,
			 String origin, String destination) {
		
		Airport origAir = airports.get(origin);
		Airport destAir = airports.get(destination);
		if (origAir == null || destAir == null) {
			return; // podria devolver boolean para indicar si se pudo agregar el vuelo o no.
		}
		Flight newFlight = new Flight(airline, number, price, departures, duration, origAir, destAir);
		flights.put(newFlight.getId(), newFlight);

		if (! origAir.routeExistsTo(destAir.getId())) {
			Route r = new Route(origAir, destAir);
			origAir.addRoute(destAir.getId(), r);
			destAir.addRoute(origAir.getId(), r);
		} else{
			origAir.addFlight2(newFlight);
			destAir.addFlight2(newFlight);
		}
		
		

}

	
}
