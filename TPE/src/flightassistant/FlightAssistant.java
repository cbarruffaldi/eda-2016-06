package flightassistant;

import java.util.Comparator;

import structures.SimpleHashMap;

public class FlightAssistant {

	//Coleccion de Aeropuertos;
	private SimpleHashMap<String, Airport> airports;

	public FlightAssistant() {
		airports = new SimpleHashMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
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
