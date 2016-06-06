package flightassistant;

import structures.SimpleHashMap;
import utils.Day;
import utils.Moment;
import utils.Time;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FlightAssistant implements Serializable {

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
	public void insertFlight(String airline, int number, double price, List<Moment> departures, Time duration,
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
		Flight newFlight = new Flight(airline, number, price, departures, duration, origAir, destAir);
		flights.put(newFlight.getId(), newFlight);

		origAir.addFlight(newFlight);
	}

	// PROVISORIO PARA TESTS
	public void insertFlight(String airline, int number, double price, Moment[] departures, Time duration,
							 String origin, String destination) {

		Airport origAir = airports.get(origin);
		Airport destAir = airports.get(destination);
		List<Moment> departuresList = new LinkedList<Moment>();
		for(Moment moment : departures)
			departuresList.add(moment);
		if (origAir == null || destAir == null) {
			return; // podria devolver boolean para indicar si se pudo agregar el vuelo o no.
		}
		// Crea la ruta en caso de que todavía no exista
		if (!origAir.routeExistsTo(destAir)) {
			Route r = new Route(origAir, destAir);
			origAir.addRoute(destAir, r);
			destAir.addRoute(origAir, r);
		}
		Flight newFlight = new Flight(airline, number, price, departuresList, duration, origAir, destAir);
		// En este mapa se guardan sin tener en cuenta el día de partida.
		flights.put(newFlight.getId(), newFlight);

		origAir.addFlight(newFlight);
	}

	public void removeFlight(String airline, int number) {
		FlightId flightId = new FlightId(airline, number);
		Flight flight = flights.get(flightId);
		if (flight != null) {
			Airport origAir = flight.getOrigin();
			Airport destAir = flight.getDestination();
			origAir.removeFlight(flight);
			flights.remove(flightId);

			// Borra la ruta entre los aeropuertos si no quedan mas vuelos entre ellos.
			Route flightRoute = origAir.getRouteTo(destAir);
			if (!flightRoute.hasFlights()) {
				origAir.removeRouteTo(destAir);
				destAir.removeRouteTo(origAir);
			}
		}
	}

	public void removeAirport(String id) {
		Airport airport = airports.get(id);
		if (airport != null) {
			removeRoutesTo(airport);
			airports.remove(id);
		}
	}

	public void removeAllAirports() {
		airports.clear();
		flights.clear();
	}

	public void removeAllFlights() {
		flights.clear();
		Iterator<Airport> iter = airports.valueIterator();
		while (iter.hasNext()) {
			Airport airport = iter.next();
			airport.removeAllRoutes();
		}
	}

	public List<Airport> findQuickestPath(String orig, String dest, List<Day> days) {
		return findPath(orig, dest, days, AirtimeWeighter.WEIGHTER, new OriginAirtimeWeighter(days));
	}

	public List<Airport> findCheapestPath(String orig, String dest, List<Day> days) {
		return findPath(orig, dest, days, PriceWeighter.WEIGHTER, new OriginPriceWeighter(days));
	}

//	public List<Airport> findShortestTotalTimeRoute(String orig, String dest, List<Day> days) {
//
//	}

	private List<Airport> findPath(String orig, String dest, List<Day> days, Weighter weighter, Weighter originWeighter) {
		Airport from = airports.get(orig);
		Airport to = airports.get(dest);
		if (from == null || to == null || from.equals(to)) {
			return null; // Ver que hacer
		}
		if (days.isEmpty()) {
			return InfinityDijkstra.minPath(this, from, to, weighter, days);
		}
		return InfinityDijkstra.minPath2(this, from, to, weighter, originWeighter, days);

	}


	private void removeRoutesTo(Airport airport) {
		Iterator<Airport> iter = airport.connectedAirportsIterator();
		while (iter.hasNext())
			iter.next().removeRouteTo(airport);
	}


	public void refreshAirportsNodeProperties() {
		Iterator<Airport> airportsIter = airports.valueIterator();
		while(airportsIter.hasNext())
			airportsIter.next().nodeRefresh();
	}
}