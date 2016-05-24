package flightassistant;

import java.util.Comparator;

import structures.SimpleHashMap;

public class FlightAssistant {
	private static final int AIRPORTS_SIZE = 20;
	private static final int FLIGHTS_SIZE = 50; // TODO: pensar tamaños

	// Coleccion de Aeropuertos;
	private SimpleHashMap<String, Airport> airports;
	// Coleccion de vuelos
	private SimpleHashMap<FlightId, Flight> flights;

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
}
